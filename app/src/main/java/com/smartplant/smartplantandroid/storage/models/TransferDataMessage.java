package com.smartplant.smartplantandroid.storage.models;

import androidx.annotation.Nullable;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class TransferDataMessage {
    @SerializedName("created_at")
    private final Date _createdAt;

    @SerializedName("sender_device_id")
    private final String _senderDeviceId;

    @SerializedName("data")
    private final @Nullable JsonObject _data;

    public TransferDataMessage(Date createdAt, String senderDeviceId, @Nullable JsonObject data) {
        this._createdAt = createdAt;
        this._senderDeviceId = senderDeviceId;
        this._data = data;
    }

    public Date getCreatedAt() {
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
