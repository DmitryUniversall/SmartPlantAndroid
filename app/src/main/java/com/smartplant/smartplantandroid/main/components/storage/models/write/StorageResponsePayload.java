package com.smartplant.smartplantandroid.main.components.storage.models.write;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class StorageResponsePayload extends StorageWritePayload {
    @SerializedName("response_to_request_uuid")
    private final @NonNull String _responseToRequestUUID;

    public StorageResponsePayload(@NonNull String responseToRequestUUID, int targetId, @Nullable JsonObject data) {
        super(targetId, data);
        this._responseToRequestUUID = responseToRequestUUID;
    }

    @NonNull
    public String getResponseToRequestUUID() {
        return _responseToRequestUUID;
    }
}
