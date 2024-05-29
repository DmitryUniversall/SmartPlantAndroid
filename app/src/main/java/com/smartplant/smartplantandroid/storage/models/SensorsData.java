package com.smartplant.smartplantandroid.storage.models;

import com.google.gson.annotations.SerializedName;

public class SensorsData {
    @SerializedName("illumination")
    private final int _illumination;

    @SerializedName("soil_moisture")
    private final int _soilMoisture;

    @SerializedName("temperature")
    private final double _temperature;

    @SerializedName("humidity")
    private final double _humidity;

    public SensorsData(int illumination, int soilMoisture, double temperature, double humidity) {
        this._illumination = illumination;
        this._soilMoisture = soilMoisture;
        this._temperature = temperature;
        this._humidity = humidity;
    }

    public int getIllumination() {
        return _illumination;
    }

    public int getSoilMoisture() {
        return _soilMoisture;
    }

    public double getTemperature() {
        return _temperature;
    }

    public double getHumidity() {
        return _humidity;
    }
}
