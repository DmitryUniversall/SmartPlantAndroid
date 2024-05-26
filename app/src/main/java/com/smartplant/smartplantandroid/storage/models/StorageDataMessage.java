package com.smartplant.smartplantandroid.storage.models;

import androidx.annotation.Nullable;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;

public class StorageDataMessage {
    @SerializedName("created_at")
    private final LocalDateTime _createdAt;

    @SerializedName("sender_device_id")
    private final String _senderDeviceId;

    @SerializedName("data")
    private final @Nullable JsonObject _data;

    public StorageDataMessage(LocalDateTime createdAt, String senderDeviceId, @Nullable JsonObject data) {
        this._createdAt = createdAt;
        this._senderDeviceId = senderDeviceId;
        this._data = data;
    }

    public LocalDateTime getCreatedAt() {
        return _createdAt;
    }

    public String getSenderDeviceId() {
        return _senderDeviceId;
    }

    @Nullable
    public JsonObject getData() {
        return _data;
    }
}
