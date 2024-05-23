package com.smartplant.smartplantandroid.utils.network.http;

import android.util.Pair;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.smartplant.smartplantandroid.utils.network.TransferResponse;
import com.smartplant.smartplantandroid.utils.network.http.excpetions.BadResponseException;
import com.smartplant.smartplantandroid.utils.network.http.excpetions.HttpTransferResponseException;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ApiHTTPRequest {
    private final Request _request;
    private final Gson _gson;
    private final OkHttpClient _client;

    public ApiHTTPRequest(Request request, Gson gson, OkHttpClient client) {
        this._request = request;
        this._gson = gson;
        this._client = client;
    }

    public Pair<Response, TransferResponse> execute() throws IOException, HttpTransferResponseException, BadResponseException {
        Response response = _client.newCall(this._request).execute();
        ResponseBody body = response.body();
        if (body == null) throw new BadResponseException("Invalid response received");

        TransferResponse transferResponse;  // FIXME: Use builder?
        try {
            transferResponse = _gson.fromJson(body.string(), TransferResponse.class);
        } catch (JsonSyntaxException e) {
            throw new BadResponseException("Invalid response received: Unable to parse json");
        }

        if (!transferResponse.isOk() || !response.isSuccessful())
            throw new HttpTransferResponseException(response, transferResponse);
        return new Pair<>(response, transferResponse);
    }
}
