package com.smartplant.smartplantandroid.storage.models;

import androidx.annotation.Nullable;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class ActionRequest {
    @SerializedName("action")
    private final DeviceAction _action;

    @SerializedName("data")
    private final @Nullable JsonObject _data;

    private ActionRequest(DeviceAction action, @Nullable JsonObject data) {
        this._action = action;
        this._data = data;
    }

    public DeviceAction getAction() {
        return _action;
    }

    @Nullable
    public JsonObject getData() {
        return _data;
    }
}
