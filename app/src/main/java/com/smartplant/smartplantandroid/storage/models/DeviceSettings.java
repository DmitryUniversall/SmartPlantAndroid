package com.smartplant.smartplantandroid.storage.models;

public class DeviceSettings {
    int data_update_interval;
    int watering_duration;

    public DeviceSettings(int data_update_interval, int watering_duration) {
        this.data_update_interval = data_update_interval;
        this.watering_duration = watering_duration;
    }
}
