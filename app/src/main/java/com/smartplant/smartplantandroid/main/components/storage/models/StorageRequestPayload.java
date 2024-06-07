package com.smartplant.smartplantandroid.main.components.storage.models;

import androidx.annotation.Nullable;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.UUID;

public class StorageRequestPayload {
    public enum RequestType {
        ENQUEUE_REQUEST(10),
        ENQUEUE_RESPONSE(11);

        private final int value;

        RequestType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    @SerializedName("request_type")
    private final int _requestType;

    @SerializedName("message_id")
    private final String _messageId;

    @SerializedName("target_user_id")
    private final int _targetId;

    @SerializedName("data")
    private @Nullable JsonObject _data;

    public StorageRequestPayload(int msgType, int targetId, @Nullable String messageId, @Nullable JsonObject data) {
        this._messageId = messageId == null ? UUID.randomUUID().toString() : messageId;
        this._requestType = msgType;
        this._targetId = targetId;
        this._data = data;
    }

    public int getMsgType() {
        return _requestType;
    }

    public String getMessageId() {
        return _messageId;
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
