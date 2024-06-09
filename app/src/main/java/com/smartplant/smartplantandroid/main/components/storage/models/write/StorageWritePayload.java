package com.smartplant.smartplantandroid.main.components.storage.models.write;

import androidx.annotation.Nullable;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class StorageWritePayload {
    @SerializedName("target_user_id")
    private final int _targetId;

    @SerializedName("data")
    private @Nullable JsonObject _data;

    public StorageWritePayload(int targetId, @Nullable JsonObject data) {
        this._targetId = targetId;
        this._data = data;
    }

    public int getTargetDeviceId() {
        return _targetId;
    }

    @Nullable
    public JsonObject getData() {
        return _data;
    }

    public void setData(@Nullable JsonObject data) {
        this._data = data;
    }
}
