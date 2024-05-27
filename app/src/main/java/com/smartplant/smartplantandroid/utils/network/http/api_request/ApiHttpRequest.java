package com.smartplant.smartplantandroid.utils.network.http.api_request;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.ReturnThis;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.smartplant.smartplantandroid.auth.exceptions.UnauthorizedException;
import com.smartplant.smartplantandroid.auth.models.AuthTokenPair;
import com.smartplant.smartplantandroid.auth.repository.AuthRepositoryST;
import com.smartplant.smartplantandroid.utils.AppLogger;
import com.smartplant.smartplantandroid.utils.data.json.JsonUtils;
import com.smartplant.smartplantandroid.utils.network.ApiHelper;
import com.smartplant.smartplantandroid.utils.network.ApplicationResponse;
import com.smartplant.smartplantandroid.utils.network.http.excpetions.BadResponseException;
import com.smartplant.smartplantandroid.utils.network.http.excpetions.CancelResponseProcess;
import com.smartplant.smartplantandroid.utils.network.http.excpetions.HttpApplicationResponseException;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ApiHttpRequest<T> {
    protected @NonNull Gson _gson;
    protected @NonNull OkHttpClient _client;
    protected @NonNull Request _request;
    protected final @NonNull ApiHttpResponseProcessor<T> _responseProcessor;
    protected final @NonNull ArrayList<ApiHttpSuccessHandler<T>> _successCallbackStack = new ArrayList<>();
    protected final @NonNull ArrayList<ApiHttpFailureHandler> _failureCallbackStack = new ArrayList<>();

    public ApiHttpRequest(@NonNull Request request, @NonNull ApiHttpResponseProcessor<T> responseProcessor, @Nullable Gson gson, @Nullable OkHttpClient client) {
        this._request = request;
        this._responseProcessor = responseProcessor;
        this._gson = gson == null ? new Gson() : gson;
        this._client = client == null ? new OkHttpClient() : client;
    }

    protected void resend() {
        this.send();
    }

    protected void resendWithNewToken() {
        this._request = ApiHelper.getAuthorizedRequestBuilder(this._request).build();
        this.resend();
    }

    protected ApiHttpRequest<AuthTokenPair> autoRefreshToken() throws UnauthorizedException {
        AuthRepositoryST authRepository = AuthRepositoryST.getInstance();
        return authRepository.refresh()
                .onSuccess(((result, response, applicationResponse) -> {
                    AppLogger.info("Tokens refreshed");
                    resendWithNewToken();
                }))
                .onFailure(((call, error) -> {
                    AppLogger.error("Failed to refresh tokens", error);
                    authRepository.logout();  // TODO: Handle it and show error message
                }));
    }


    protected void callSuccessCallStack(@NonNull T result, @NonNull Response response, @NonNull ApplicationResponse applicationResponse) {
        if (_failureCallbackStack.isEmpty()) {
            AppLogger.warning("Unprocessed request", _request.url());
            return;
        }

        _successCallbackStack.forEach((handler) -> handler.onSuccess(result, response, applicationResponse));
    }

    protected void callFailureCallStack(@NonNull Call call, @NonNull Throwable error) {
        if (_failureCallbackStack.isEmpty()) {
            AppLogger.error("Unprocessed request error", error);
            return;
        }

        Throwable currentError = error;

        int stackSize = _failureCallbackStack.size();
        for (int index = 0; index < stackSize; index++) {
            ApiHttpFailureHandler handler = _failureCallbackStack.get(index);

            try {
                handler.onFailure(call, currentError);
            } catch (Throwable e) {
                // Error occurred in last handler, so it will not be processed and will be lost
                if (index == stackSize - 1) AppLogger.error("Unprocessed request error", e);
                currentError = e;
            }
        }
    }

    protected ApplicationResponse getTransferResponse(@NonNull Response response) throws IOException, UnauthorizedException, HttpApplicationResponseException, BadResponseException, CancelResponseProcess {
        ResponseBody body = response.body();
        if (body == null) throw new BadResponseException("Invalid response received");

        ApplicationResponse applicationResponse;
        try {
            String responseBodyString = body.string();
            if (responseBodyString.trim().isEmpty())
                throw new BadResponseException("Empty response received");
            applicationResponse = JsonUtils.fromJsonWithNulls(responseBodyString, ApplicationResponse.class, _gson);
        } catch (JsonSyntaxException e) {
            throw new BadResponseException("Invalid response received: Unable to parse json", e);
        }

        int applicationStatusCode = applicationResponse.getApplicationStatusCode();
        if (applicationStatusCode == 3002 || applicationStatusCode == 3003) {  // Invalid or expired
            this.autoRefreshToken().onSuccess(((result, response1, applicationResponse1) -> this.resendWithNewToken())).send();
            throw new CancelResponseProcess();
        }

        if (!applicationResponse.isOk() || !response.isSuccessful())
            throw new HttpApplicationResponseException(response, applicationResponse);

        return applicationResponse;
    }

    protected void processFailure(@NonNull Call call, @NonNull Throwable error) {
        this.callFailureCallStack(call, error);
    }

    protected void processResponse(@NonNull Call call, @NonNull Response response) throws IOException {
        try {
            ApplicationResponse applicationResponse = getTransferResponse(response);
            T result = _responseProcessor.processResponse(response, applicationResponse);
            this.callSuccessCallStack(result, response, applicationResponse);
        } catch (HttpApplicationResponseException | BadResponseException |
                 UnauthorizedException error) {
            processFailure(call, error);
        } catch (CancelResponseProcess e) {
            return;  // Ignore it. Request should be canceled or it will be resend later
        }
    }

    public void send() {
        _client.newCall(this._request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                processFailure(call, e);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                processResponse(call, response);
            }
        });
    }

    @ReturnThis
    public ApiHttpRequest<T> onSuccess(ApiHttpSuccessHandler<T> handler) {
        this._successCallbackStack.add(handler);
        return this;
    }

    @ReturnThis
    public ApiHttpRequest<T> onFailure(ApiHttpFailureHandler handler) {
        _failureCallbackStack.add(handler);
        return this;
    }

    @ReturnThis
    public ApiHttpRequest<T> setGson(Gson _gson) {
        this._gson = _gson;
        return this;
    }

    @ReturnThis
    public ApiHttpRequest<T> setClient(OkHttpClient _client) {
        this._client = _client;
        return this;
    }
}
