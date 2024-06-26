package com.smartplant.smartplantandroid.main.components.storage.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

    @SerializedName("sender_user_id")
    private final int _senderId;

    @SerializedName("request_uuid")
    private final @NonNull String _requestUUID;

    @SerializedName("created_at")
    private final Date _createdAt;

    @SerializedName("data")
    private final JsonObject _data;

    public StorageDataMessage(int dataType, int senderId, @NonNull String requestUUID, @NonNull Date createdAt, @Nullable JsonObject data) {
        this._dataType = dataType;
        this._senderId = senderId;
        this._requestUUID = requestUUID;
        this._createdAt = createdAt;
        this._data = data;
    }

    public int getDataType() {
        return _dataType;
    }

    public int getSenderId() {
        return _senderId;
    }

    @NonNull
    public String getRequestUUID() {
        return _requestUUID;
    }

    public Date getCreatedAt() {
        return _createdAt;
    }

    public JsonObject getData() {
        return _data;
    }
}
