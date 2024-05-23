package com.smartplant.smartplantandroid.storage.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class TransferDataMessage {
    @SerializedName("created_at")
    private final @NonNull Date _createdAt;

    @SerializedName("sender_device_id")
    private final @NonNull String _senderDeviceId;

    @SerializedName("data")
    private final @Nullable JsonObject _data;

    public TransferDataMessage(@NonNull Date createdAt, @NonNull String senderDeviceId, @Nullable JsonObject data) {
        this._createdAt = createdAt;
        this._senderDeviceId = senderDeviceId;
        this._data = data;
    }

    @NonNull
    public Date getCreatedAt() {
        return _createdAt;
    }

    @NonNull
    public String getSenderDeviceId() {
        return _senderDeviceId;
    }

    @Nullable
    public JsonObject getData() {
        return _data;
    }
}
