package com.smartplant.smartplantandroid.utils.network.http.api_request;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.ReturnThis;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.smartplant.smartplantandroid.utils.AppLogger;
import com.smartplant.smartplantandroid.utils.network.TransferResponse;
import com.smartplant.smartplantandroid.utils.network.http.excpetions.BadResponseException;
import com.smartplant.smartplantandroid.utils.network.http.excpetions.HttpTransferResponseException;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ApiHttpRequest<T> {
    private @NonNull Gson _gson;
    private @NonNull OkHttpClient _client;
    private final @NonNull Request _request;
    private final @NonNull ApiHttpResponseProcessor<T> _responseProcessor;
    private final @NonNull ArrayList<ApiHttpSuccessHandler<T>> _successCallbackStack = new ArrayList<>();
    private final @NonNull ArrayList<ApiHttpFailureHandler> _failureCallbackStack = new ArrayList<>();

    public ApiHttpRequest(@NonNull Request request, @NonNull ApiHttpResponseProcessor<T> responseProcessor, @Nullable Gson gson, @Nullable OkHttpClient client) {
        this._request = request;
        this._responseProcessor = responseProcessor;
        this._gson = gson == null ? new Gson() : gson;
        this._client = client == null ? new OkHttpClient() : client;
    }

    protected void callSuccessCallStack(@NonNull T result, @NonNull Response response, @NonNull TransferResponse transferResponse) {
        if (_failureCallbackStack.isEmpty()) {
            AppLogger.warning("Unprocessed request %s", _request.url());
            return;
        }

        _successCallbackStack.forEach((handler) -> handler.onSuccess(result, response, transferResponse));
    }

    protected void callFailureCallStack(@NonNull Call call, @NonNull Throwable error) {
        if (_failureCallbackStack.isEmpty()) {
            AppLogger.error("Unprocessed request error %s", error);
            return;
        }

        Throwable currentError = error;

        for (ApiHttpFailureHandler handler : _failureCallbackStack) {
            try {
                handler.onFailure(call, currentError);
            } catch (Throwable e) {
                currentError = e;
            }
        }
    }

    protected TransferResponse getTransferResponse(@NonNull Response response) throws IOException, HttpTransferResponseException, BadResponseException {
        ResponseBody body = response.body();
        if (body == null) throw new BadResponseException("Invalid response received");

        TransferResponse transferResponse;
        try {
            String responseBodyString = body.string();
            if (responseBodyString.trim().isEmpty()) throw new BadResponseException("Empty response received");

            JsonObject json = JsonParser.parseString(responseBodyString).getAsJsonObject();
            boolean dataIsNull = json.get("data").isJsonNull();
            if (dataIsNull) json.add("data", new JsonObject());
            transferResponse = _gson.fromJson(json, TransferResponse.class);
            if (dataIsNull) transferResponse.setData(null);
        } catch (JsonSyntaxException e) {
            throw new BadResponseException("Invalid response received: Unable to parse json", e);
        }

        if (!transferResponse.isOk() || !response.isSuccessful())
            throw new HttpTransferResponseException(response, transferResponse);

        return transferResponse;
    }

    protected void processFailure(@NonNull Call call, @NonNull Throwable error) {
        this.callFailureCallStack(call, error);
    }

    protected void processResponse(@NonNull Call call, @NonNull Response response) throws IOException {
        try {
            TransferResponse transferResponse = getTransferResponse(response);
            T result = _responseProcessor.processResponse(response, transferResponse);
            this.callSuccessCallStack(result, response, transferResponse);
        } catch (HttpTransferResponseException | BadResponseException error) {
            processFailure(call, error);
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
