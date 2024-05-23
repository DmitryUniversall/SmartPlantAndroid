package com.smartplant.smartplantandroid.utils.network;

import com.google.gson.JsonObject;

import androidx.annotation.NonNull;
import org.jetbrains.annotations.Nullable;


public class TransferResponse {
    private final boolean _ok;
    private final int _applicationStatusCode;
    private final @NonNull String _message;
    private final @Nullable JsonObject _data;

    public TransferResponse(boolean ok, int applicationStatusCode, @NonNull String message, @Nullable JsonObject data) {
        this._ok = ok;
        this._applicationStatusCode = applicationStatusCode;
        this._message = message;
        this._data = data;
    }

    public boolean isOk() {
        return _ok;
    }

    public int getApplicationStatusCode() {
        return _applicationStatusCode;
    }

    @NonNull
    public String getMessage() {
        return _message;
    }

    @Nullable
    public JsonObject getData() {
        return _data;
    }
}
