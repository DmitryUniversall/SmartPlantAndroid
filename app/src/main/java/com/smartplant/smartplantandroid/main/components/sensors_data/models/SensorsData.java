package com.smartplant.smartplantandroid.main.components.sensors_data.models;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class SensorsData {
    @SerializedName("id")
    private final @Nullable Integer _id;

    @SerializedName("water_level")
    private final int _waterLevel;

    @SerializedName("illumination")
    private final int _illumination;

    @SerializedName("soil_moisture")
    private final int _soilMoisture;

    @SerializedName("temperature")
    private final double _temperature;

    @SerializedName("humidity")
    private final double _humidity;

    @SerializedName("created_at")
    private final Date _createdAt;

    public SensorsData(@Nullable Integer id, int waterLevel, int illumination, int soilMoisture, double temperature, double humidity, @Nullable Date createdAt) {
        this._id = id;
        this._waterLevel = waterLevel;
        this._illumination = illumination;
        this._soilMoisture = soilMoisture;
        this._temperature = temperature;
        this._humidity = humidity;
        this._createdAt = createdAt != null ? createdAt : new Date();
    }

    @Nullable
    public Integer getId() {
        return _id;
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

    public int getWaterLevel() {
        return _waterLevel;
    }

    public Date getCreatedAt() {
        return _createdAt;
    }
}
