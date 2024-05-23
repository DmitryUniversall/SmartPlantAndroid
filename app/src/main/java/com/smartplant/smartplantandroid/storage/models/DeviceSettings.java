package com.smartplant.smartplantandroid.storage.models;

import com.google.gson.annotations.SerializedName;

public class DeviceSettings {
    @SerializedName("watering_duration")
    private int _wateringDuration;

    public DeviceSettings(int watering_duration) {
        this._wateringDuration = watering_duration;
    }

    public int getWateringDuration() {
        return _wateringDuration;
    }

    public void setWateringDuration(int _wateringDuration) {
        this._wateringDuration = _wateringDuration;
    }
}
