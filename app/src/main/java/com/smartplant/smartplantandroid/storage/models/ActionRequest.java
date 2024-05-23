package com.smartplant.smartplantandroid.storage.models;

import androidx.annotation.Nullable;

import com.google.gson.JsonObject;

public class ActionRequest {
    public final DeviceAction action;
    public final @Nullable JsonObject data;

    public ActionRequest(DeviceAction action, @Nullable JsonObject data) {
        this.action = action;
        this.data = data;
    }
}
