package com.smartplant.smartplantandroid.storage.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class ActionResponse {
    @SerializedName("ok")
    private final boolean _ok;

    @SerializedName("application_status_code")
    private final int _applicationStatusCode;

    @SerializedName("message")
    private final @NonNull String _message;

    @SerializedName("data")
    private final @Nullable JsonObject _data;

    public ActionResponse(boolean ok, int applicationStatusCode, @NonNull String message, @Nullable JsonObject data) {
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
