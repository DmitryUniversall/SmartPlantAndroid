package com.smartplant.smartplantandroid.storage.models;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class StorageDataMessage {
    public enum MessageDataType {
        REQUEST(1),
        RESPONSE(2);

        private final int _value;

        MessageDataType(int value) {
            this._value = value;
        }

        public int getValue() {
            return _value;
        }
    }

    @SerializedName("data_type")
    private final int _dataType;

    @SerializedName("message_id")
    private final String _messageId;

    @SerializedName("created_at")
    private final Date _createdAt;

    @SerializedName("sender_device_id")
    private final String _senderDeviceId;

    @SerializedName("data")
    private final JsonObject _data;

    public StorageDataMessage(String messageId, int dataType, Date createdAt, String senderDeviceId, JsonObject data) {
        this._messageId = messageId;
        this._dataType = dataType;
        this._createdAt = createdAt;
        this._senderDeviceId = senderDeviceId;
        this._data = data;
    }

    public int getDataType() {
        return _dataType;
    }

    public String getMessageId() {
        return _messageId;
    }

    public Date getCreatedAt() {
        return _createdAt;
    }

    public String getSenderDeviceId() {
        return _senderDeviceId;
    }

    public JsonObject getData() {
        return _data;
    }
}
