package com.smartplant.smartplantandroid.utils.network.http.excpetions;

import com.smartplant.smartplantandroid.utils.network.TransferResponse;

import okhttp3.Response;

public class HttpTransferResponseException extends Exception {
    private final Response _response;
    private final TransferResponse _transferResponse;

    public HttpTransferResponseException(Response response, TransferResponse transferResponse) {
        this._response = response;
        this._transferResponse = transferResponse;
    }

    public Response getResponse() {
        return _response;
    }

    public TransferResponse getTransferResponse() {
        return _transferResponse;
    }
}
