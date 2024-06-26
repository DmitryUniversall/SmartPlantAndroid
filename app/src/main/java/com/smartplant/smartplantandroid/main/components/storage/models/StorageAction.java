package com.smartplant.smartplantandroid.main.components.storage.models;

import androidx.annotation.Nullable;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class StorageAction {
    public enum ApplicationActionType {  // Can be sent by application (this device)
        REQUEST_SENSORS_DATA(1),
        REQUEST_SENSORS_DATA_UPDATE(2),
        COMMAND_STOP_DATA_UPDATE(3),
        COMMAND_SETTINGS_UPDATE(4),
        COMMAND_IRRIGATE(5),
        COMMAND_LAMP(6);

        private final int _value;

        ApplicationActionType(int value) {
            this._value = value;
        }

        public int getValue() {
            return _value;
        }
    }

    public enum DeviceActionType {  // Can be sent by device
        PING(101),
        SENSORS_DATA_UPDATE(102);

        private final int _value;

        DeviceActionType(int value) {
            this._value = value;
        }

        public int getValue() {
            return _value;
        }
    }

    @SerializedName("action")
    private final int _action;

    @SerializedName("data")
    private final @Nullable JsonObject _data;

    public StorageAction(int action, @Nullable JsonObject data) {
        this._action = action;
        this._data = data;
    }

    public int getAction() {
        return _action;
    }

    @Nullable
    public JsonObject getData() {
        return _data;
    }
}
