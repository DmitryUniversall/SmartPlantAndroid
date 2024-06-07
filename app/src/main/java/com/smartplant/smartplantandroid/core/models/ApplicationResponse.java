package com.smartplant.smartplantandroid.core.models;

import androidx.annotation.Nullable;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.smartplant.smartplantandroid.core.network.ApplicationStatusCodes;


public class ApplicationResponse {
    @SerializedName("ok")
    private final boolean _ok;

    @SerializedName("application_status_code")
    private final int _applicationStatusCode;

    @SerializedName("message")
    private final String _message;

    @SerializedName("data")
    private final @Nullable JsonObject _data;

    public ApplicationResponse(boolean ok, int applicationStatusCode, String message, @Nullable JsonObject data) {
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

    public ApplicationStatusCodes.StatusCode getStatusCode() {
        return ApplicationStatusCodes.findStatusCodeByCode(this._applicationStatusCode, null);
    }

    public String getMessage() {
        return _message;
    }

    @Nullable
    public JsonObject getData() {
        return _data;
    }
}
