package com.smartplant.smartplantandroid.core.exceptions.http;

import androidx.annotation.Nullable;

import okhttp3.Response;

public class BadResponseException extends Exception {
    private final @Nullable Response _response;

    public BadResponseException(String message, @Nullable Response response) {
        super(message);
        this._response = response;
    }

    public BadResponseException(String message, Throwable error, @Nullable Response response) {
        super(message, error);
        this._response = response;
    }

    @Nullable
    public Response getResponse() {
        return _response;
    }
}
