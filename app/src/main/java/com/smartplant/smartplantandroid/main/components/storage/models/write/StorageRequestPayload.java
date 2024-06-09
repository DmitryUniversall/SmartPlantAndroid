package com.smartplant.smartplantandroid.main.components.storage.models.write;

import androidx.annotation.Nullable;

import com.google.gson.JsonObject;


public class StorageRequestPayload extends StorageWritePayload{
    public StorageRequestPayload(int targetId, @Nullable JsonObject data) {
        super(targetId, data);
    }
}
