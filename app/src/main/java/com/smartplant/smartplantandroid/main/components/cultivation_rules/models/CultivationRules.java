package com.smartplant.smartplantandroid.main.components.cultivation_rules.models;

public class CultivationRules {
    // Device
    private final int _deviceId;

    // Rules
    private int minTemperature = -1;
    private int maxTemperature = -1;

    private int minHumidityPercent = -1;
    private int maxHumidityPercent = -1;

    private int minSoilMoisturePercent = -1;
    private int maxSoilMoisturePercent = -1;

    private int minIlluminationPercent = -1;
    private int maxIlluminationPercent = -1;

    public CultivationRules(int deviceId) {
        this._deviceId = deviceId;
    }

    public int getDeviceId() {
        return _deviceId;
    }

    public int getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(int minTemperature) {
        this.minTemperature = minTemperature;
    }

    public int getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(int maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public int getMinHumidityPercent() {
        return minHumidityPercent;
    }

    public void setMinHumidityPercent(int minHumidityPercent) {
        this.minHumidityPercent = minHumidityPercent;
    }

    public int getMaxHumidityPercent() {
        return maxHumidityPercent;
    }

    public void setMaxHumidityPercent(int maxHumidityPercent) {
        this.maxHumidityPercent = maxHumidityPercent;
    }

    public int getMinSoilMoisturePercent() {
        return minSoilMoisturePercent;
    }

    public void setMinSoilMoisturePercent(int minSoilMoisturePercent) {
        this.minSoilMoisturePercent = minSoilMoisturePercent;
    }

    public int getMaxSoilMoisturePercent() {
        return maxSoilMoisturePercent;
    }

    public void setMaxSoilMoisturePercent(int maxSoilMoisturePercent) {
        this.maxSoilMoisturePercent = maxSoilMoisturePercent;
    }

    public int getMinIlluminationPercent() {
        return minIlluminationPercent;
    }

    public void setMinIlluminationPercent(int minIlluminationPercent) {
        this.minIlluminationPercent = minIlluminationPercent;
    }

    public int getMaxIlluminationPercent() {
        return maxIlluminationPercent;
    }

    public void setMaxIlluminationPercent(int maxIlluminationPercent) {
        this.maxIlluminationPercent = maxIlluminationPercent;
    }
}
