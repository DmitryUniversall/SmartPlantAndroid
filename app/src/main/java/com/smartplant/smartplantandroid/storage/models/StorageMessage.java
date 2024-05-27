package com.smartplant.smartplantandroid.storage.models;

import androidx.annotation.Nullable;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class StorageMessage {
    public enum StorageResponseMSGType {
        APPLICATION_RESPONSE(1),
        DATA_MESSAGE(2);

        private final int value;

        StorageResponseMSGType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    @SerializedName("msg_type")
    private final int _msgType;

    @SerializedName("data")
    private final @Nullable JsonObject _data;

    public StorageMessage(int msgType, @Nullable JsonObject data) {
        this._msgType = msgType;
        this._data = data;
    }

    public int get_msgType() {
        return _msgType;
    }

    @Nullable
    public JsonObject get_data() {
        return _data;
    }
}
