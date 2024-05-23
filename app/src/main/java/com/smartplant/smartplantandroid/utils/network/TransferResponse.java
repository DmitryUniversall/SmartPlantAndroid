package com.smartplant.smartplantandroid.utils.network;

import com.google.gson.JsonObject;

import androidx.annotation.Nullable;


//public static class Builder {
//    private Boolean ok;
//    private Integer applicationStatusCode;
//    private String message;
//    private JsonObject data;
//
//    public void setOk(boolean ok) {
//        this.ok = ok;
//    }
//
//    public void setApplicationStatusCode(int applicationStatusCode) {
//        this.applicationStatusCode = applicationStatusCode;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }
//
//    public void setData(JsonObject data) {
//        this.data = data;
//    }
//
//    public TransferResponse build() throws BuildFailedException {
//        if (ok == null || applicationStatusCode == null || message == null)
//            throw new BuildFailedException("Some of required fields were not specified");
//
//        return new TransferResponse(ok, applicationStatusCode, message, data);
//    }
//}

public class TransferResponse {
    private final boolean _ok;
    private final int _applicationStatusCode;
    private final String _message;
    private final @Nullable JsonObject _data;

    public TransferResponse(boolean ok, int applicationStatusCode, String message, @Nullable JsonObject data) {
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

    public String getMessage() {
        return _message;
    }

    @Nullable
    public JsonObject getData() {
        return _data;
    }
}
