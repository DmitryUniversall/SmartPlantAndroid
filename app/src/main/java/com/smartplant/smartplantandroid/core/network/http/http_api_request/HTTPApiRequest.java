package com.smartplant.smartplantandroid.core.network.http.http_api_request;

import static com.smartplant.smartplantandroid.core.data.json.JsonUtils.getGson;
import static com.smartplant.smartplantandroid.core.network.ApiUtils.getAuthorizedRequestBuilder;
import static com.smartplant.smartplantandroid.core.network.ApiUtils.getOkHttpClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.ReturnThis;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.smartplant.smartplantandroid.core.data.json.JsonUtils;
import com.smartplant.smartplantandroid.core.exceptions.CancelException;
import com.smartplant.smartplantandroid.core.exceptions.http.BadResponseException;
import com.smartplant.smartplantandroid.core.exceptions.http.HttpApplicationResponseException;
import com.smartplant.smartplantandroid.core.logs.AppLogger;
import com.smartplant.smartplantandroid.core.models.ApplicationResponse;
import com.smartplant.smartplantandroid.core.network.ApplicationStatusCodes;
import com.smartplant.smartplantandroid.main.components.auth.exceptions.UnauthorizedException;
import com.smartplant.smartplantandroid.main.components.auth.models.AuthTokenPair;
import com.smartplant.smartplantandroid.main.components.auth.repository.AuthRepositoryST;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HTTPApiRequest<T> {
    // Utils
    protected final @NonNull Gson _gson;
    protected final @NonNull OkHttpClient _client;

    // Disposable callbacks
    protected final @NonNull Queue<HTTPApiSuccessCallback<T>> _successCallbacks = new LinkedList<>();
    protected final @NonNull Queue<HTTPApiFailureCallback> _failureCallbacks = new LinkedList<>();

    // Other
    protected final @NonNull HTTPApiResponseProcessor<T> _responseProcessor;
    protected @NonNull Request _request;

    public HTTPApiRequest(@NonNull Request request, @NonNull HTTPApiResponseProcessor<T> responseProcessor, @Nullable Gson gson, @Nullable OkHttpClient client) {
        this._request = request;
        this._responseProcessor = responseProcessor;
        this._gson = gson == null ? getGson() : gson;
        this._client = client == null ? getOkHttpClient() : client;
    }

    protected HTTPApiRequest<AuthTokenPair> _refreshToken() {
        AuthRepositoryST authRepository = AuthRepositoryST.getInstance();
        return authRepository.refresh()
                .onSuccess(((result, response, applicationResponse) -> AppLogger.info("Tokens refreshed")))
                .onFailure(((call, error) -> {
                    AppLogger.error("Failed to refresh tokens", error);
                    authRepository.logout();  // TODO: Handle it and show error message
                }));
    }

    protected void _resend() {
        this.send();
    }

    protected void _resendWithNewToken() {
        this._request = getAuthorizedRequestBuilder(this._request).build();
        this._resend();
    }

    protected void _refreshTokenAndResend() {
        this._refreshToken().onSuccess(((result, response1, applicationResponse1) -> this._resendWithNewToken())).send();
    }

    protected void _callSuccessCallbacks(@NonNull T result, @NonNull Response response, @NonNull ApplicationResponse applicationResponse) {
        if (this._successCallbacks.isEmpty()) {
            AppLogger.warning("Unprocessed request", this._request.url());
            return;
        }

        this._successCallbacks.forEach((handler) -> handler.onSuccess(result, response, applicationResponse));
        this._successCallbacks.clear();
    }

    protected void _callFailureCallbacks(@NonNull Call call, @NonNull Throwable error) {  // TODO: Make it as global util
        if (this._failureCallbacks.isEmpty()) {
            AppLogger.error("Unprocessed request error", error);
            return;
        }

        Throwable currentError = error;
        while (!this._failureCallbacks.isEmpty()) {
            HTTPApiFailureCallback handler = this._failureCallbacks.poll();
            assert handler != null;

            try {
                handler.onFailure(call, currentError);
            } catch (Throwable e) {
                // Error occurred in last handler, so it will not be processed and will be lost
                if (this._failureCallbacks.isEmpty())
                    AppLogger.error("Unprocessed request error", e);
                currentError = e;
            }
        }
    }

    protected ApplicationResponse _getApplicationResponse(@NonNull Response response) throws IOException, UnauthorizedException, HttpApplicationResponseException, BadResponseException, CancelException {
        ResponseBody body = response.body();
        if (body == null) throw new BadResponseException("Invalid response received", response);

        ApplicationResponse applicationResponse;
        try {
            String responseBodyString = body.string();

            if (responseBodyString.trim().isEmpty())
                throw new BadResponseException("Empty response received", response);

            applicationResponse = JsonUtils.fromJsonWithNulls(responseBodyString, ApplicationResponse.class, _gson);
        } catch (JsonSyntaxException error) {
            throw new BadResponseException("Invalid response received: Unable to parse json", error, response);
        }

        int applicationStatusCode = applicationResponse.getApplicationStatusCode();
        if (applicationStatusCode == ApplicationStatusCodes.AUTH.TOKEN_EXPIRED.getCode()) {  // Access token expired
            this._refreshTokenAndResend();
            throw new CancelException();
        }

        if (!applicationResponse.isOk())
            throw new HttpApplicationResponseException(applicationResponse, response);

        return applicationResponse;
    }


    protected void _processFailure(@NonNull Call call, @NonNull Throwable error) {
        this._callFailureCallbacks(call, error);
    }

    protected void _processResponse(@NonNull Call call, @NonNull Response response) throws IOException {
        try {
            ApplicationResponse applicationResponse = _getApplicationResponse(response);
            T result = _responseProcessor.processResponse(response, applicationResponse);
            this._callSuccessCallbacks(result, response, applicationResponse);
        } catch (HttpApplicationResponseException | BadResponseException |
                 UnauthorizedException error) {
            _processFailure(call, error);
        } catch (CancelException e) {
            // Ignore it. Request processing should be canceled
        }
    }

    public void send() {
        _client.newCall(this._request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                _processFailure(call, e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                _processResponse(call, response);
            }
        });
    }

    @ReturnThis
    public HTTPApiRequest<T> onSuccess(HTTPApiSuccessCallback<T> callback) {
        this._successCallbacks.add(callback);
        return this;
    }

    @ReturnThis
    public HTTPApiRequest<T> onFailure(HTTPApiFailureCallback callback) {
        _failureCallbacks.add(callback);
        return this;
    }
}
