package com.smartplant.smartplantandroid.utils.network;

import com.google.gson.JsonObject;


public class TransferResponse {
    public final boolean ok;
    public final int applicationStatusCode;
    public final String message;
    public JsonObject data;

    public TransferResponse(boolean ok, int applicationStatusCode, String message, JsonObject data) {
        this.ok = ok;
        this.applicationStatusCode = applicationStatusCode;
        this.message = message;
        this.data = data;
    }
}
