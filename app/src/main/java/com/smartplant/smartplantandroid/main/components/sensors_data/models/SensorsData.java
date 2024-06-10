package com.smartplant.smartplantandroid.main.components.sensors_data.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.Objects;

public class SensorsData {
    @SerializedName("id")
    private @Nullable Long _id;

    @SerializedName("water_level")
    private int _waterLevel;

    @SerializedName("illumination")
    private int _illumination;

    @SerializedName("soil_moisture")
    private int _soilMoisture;

    @SerializedName("temperature")
    private double _temperature;

    @SerializedName("humidity")
    private double _humidity;

    @SerializedName("created_at")
    private Date _createdAt;

    public SensorsData(@Nullable Long id, int waterLevel, int illumination, int soilMoisture, double temperature, double humidity, @Nullable Date createdAt) {
        this._id = id;
        this._waterLevel = waterLevel;
        this._illumination = illumination;
        this._soilMoisture = soilMoisture;
        this._temperature = temperature;
        this._humidity = humidity;
        this._createdAt = createdAt != null ? createdAt : new Date();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SensorsData that = (SensorsData) obj;
        return _waterLevel == that._waterLevel &&
                _illumination == that._illumination &&
                _soilMoisture == that._soilMoisture &&
                Double.compare(that._temperature, _temperature) == 0 &&
                Double.compare(that._humidity, _humidity) == 0 &&
                Objects.equals(_id, that._id) &&
                Objects.equals(_createdAt, that._createdAt);
    }

    @Nullable
    public Long getId() {
        return _id;
    }

    public void setId(@Nullable Long id) {
        this._id = id;
    }

    public int getIllumination() {
        return _illumination;
    }

    public void setIllumination(int illumination) {
        this._illumination = illumination;
    }

    public int getSoilMoisture() {
        return _soilMoisture;
    }

    public void setSoilMoisture(int soilMoisture) {
        this._soilMoisture = soilMoisture;
    }

    public double getTemperature() {
        return _temperature;
    }

    public void setTemperature(double temperature) {
        this._temperature = temperature;
    }

    public double getHumidity() {
        return _humidity;
    }

    public void setHumidity(double humidity) {
        this._humidity = humidity;
    }

    public int getWaterLevel() {
        return _waterLevel;
    }

    public void setWaterLevel(int waterLevel) {
        this._waterLevel = waterLevel;
    }

    public Date getCreatedAt() {
        return _createdAt;
    }

    public void setCreatedAt(@NonNull Date createdAt) {
        this._createdAt = createdAt;
    }
}
