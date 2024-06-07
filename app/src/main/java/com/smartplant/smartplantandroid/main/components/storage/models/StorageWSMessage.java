package com.smartplant.smartplantandroid.main.components.storage.models;

import androidx.annotation.Nullable;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class StorageWSMessage {
    public enum MSGType {
        APPLICATION_RESPONSE(1),
        DATA_MESSAGE(2);

        private final int value;

        MSGType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    @SerializedName("msg_type")
    private final int _MSGType;

    @SerializedName("data")
    private final @Nullable JsonObject _data;

    public StorageWSMessage(int MSGType, @Nullable JsonObject data) {
        this._MSGType = MSGType;
        this._data = data;
    }

    public int getMSGType() {
        return _MSGType;
    }

    @Nullable
    public JsonObject getData() {
        return _data;
    }
}
