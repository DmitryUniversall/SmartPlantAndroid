package com.smartplant.smartplantandroid.storage.models;

public enum DeviceAction {
    REQUEST_SENSORS_DATA(1),
    REQUEST_SENSORS_DATA_UPDATE(2),
    COMMAND_STOP_DATA_UPDATE(3),
    COMMAND_SETTINGS_UPDATE(4),
    COMMAND_WATER(5),
    COMMAND_LAMP(6);

    private final int value;

    DeviceAction(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
