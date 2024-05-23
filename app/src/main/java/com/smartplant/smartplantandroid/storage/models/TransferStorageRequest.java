package com.smartplant.smartplantandroid.storage.models;

import androidx.annotation.Nullable;

import com.google.gson.JsonObject;

public class TransferStorageRequest {
    public final TransferRequestMSGType msgType;
    public final String targetDeviceId;
    public final @Nullable JsonObject data;

    public TransferStorageRequest(TransferRequestMSGType msgType, String targetDeviceId, @Nullable JsonObject data) {
        this.msgType = msgType;
        this.targetDeviceId = targetDeviceId;
        this.data = data;
    }
}
