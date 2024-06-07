package com.smartplant.smartplantandroid.main.components.storage.models;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class StorageDataMessage {
    public enum DataType {
        REQUEST(1),
        RESPONSE(2);

        private final int _value;

        DataType(int value) {
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

    @SerializedName("sender_id")
    private final int _senderId;

    @SerializedName("data")
    private final JsonObject _data;

    public StorageDataMessage(int dataType, String messageId, Date createdAt, int senderId, JsonObject data) {
        this._messageId = messageId;
        this._dataType = dataType;
        this._createdAt = createdAt;
        this._senderId = senderId;
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

    public int getSenderId() {
        return _senderId;
    }

    public JsonObject getData() {
        return _data;
    }
}
