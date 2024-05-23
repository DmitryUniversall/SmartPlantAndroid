package com.smartplant.smartplantandroid.storage.models;

import androidx.annotation.Nullable;

import com.google.gson.JsonObject;

import java.util.Date;

public class TransferDataMessage {
    public final Date createdAt;
    public final String senderDeviceId;
    public final @Nullable JsonObject data;

    public TransferDataMessage(Date createdAt, String senderDeviceId, @Nullable JsonObject data) {
        this.createdAt = createdAt;
        this.senderDeviceId = senderDeviceId;
        this.data = data;
    }
}
