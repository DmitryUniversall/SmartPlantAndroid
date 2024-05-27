package com.smartplant.smartplantandroid.storage.models;

import androidx.annotation.Nullable;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.UUID;

public class StorageRequest {
    public enum StorageRequestType {
        ENQUEUE_REQUEST(1),
        ENQUEUE_RESPONSE(2);

        private final int value;

        StorageRequestType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    @SerializedName("msg_type")
    private final int _msgType;

    @SerializedName("message_id")
    private final String _messageId;

    @SerializedName("target_device_id")
    private final String _targetDeviceId;

    @SerializedName("data")
    private final @Nullable JsonObject _data;

    public StorageRequest(int msgType, String targetDeviceId, @Nullable String messageId, @Nullable JsonObject data) {
        this._messageId = messageId == null ? UUID.randomUUID().toString() : messageId;
        this._msgType = msgType;
        this._targetDeviceId = targetDeviceId;
        this._data = data;
    }

    public int getMsgType() {
        return _msgType;
    }

    public String getMessageId() {
        return _messageId;
    }

    public String getTargetDeviceId() {
        return _targetDeviceId;
    }

    @Nullable
    public JsonObject getData() {
        return _data;
    }
}
