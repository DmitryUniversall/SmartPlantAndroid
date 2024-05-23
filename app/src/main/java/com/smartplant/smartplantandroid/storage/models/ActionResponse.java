package com.smartplant.smartplantandroid.storage.models;

import androidx.annotation.Nullable;

import com.google.gson.JsonObject;

public class ActionResponse {
    public final boolean ok;
    public final String message;
    public final int applicationStatusCode;
    public final @Nullable JsonObject data;

    public ActionResponse(boolean ok, String message, int applicationStatusCode, @Nullable JsonObject data) {
        this.ok = ok;
        this.message = message;
        this.applicationStatusCode = applicationStatusCode;
        this.data = data;
    }
}
