package com.smartplant.smartplantandroid.main.components.devices.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DevicesLocalDataManagerST {
    // Singleton
    private static @Nullable DevicesLocalDataManagerST _instance;

    // Preferences
    private static SharedPreferences _sharedPreferences;
    private static final String _PREFERENCES_FILE_NAME = "SmartPlantPreferencesDevices";

    public static synchronized void createInstance(Context context) {
        if (_instance != null)
            throw new RuntimeException("DevicesLocalDataManagerST has already been initialized");
        _instance = new DevicesLocalDataManagerST(context);
    }

    public static synchronized DevicesLocalDataManagerST getInstance() {
        if (_instance == null)
            throw new RuntimeException("DevicesLocalDataManagerST has not been initialized");
        return _instance;
    }

    private DevicesLocalDataManagerST(Context context) {
        _sharedPreferences = context.getSharedPreferences(_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
    }

    private String _getDeviceNameKey(int deviceId) {
        return "devicesLocal_device_" + deviceId + "_name";
    }

    private String _getDeviceDescriptionKey(int deviceId) {
        return "devicesLocal_device_" + deviceId + "_description";
    }

    private String _getDeviceIconIdKey(int deviceId) {
        return "devicesLocal_device_" + deviceId + "_iconId";
    }

    public @Nullable String getDeviceName(int deviceId) {
        return _sharedPreferences.getString(_getDeviceNameKey(deviceId), null);
    }

    public void setDeviceName(int deviceId, @NonNull String name) {
        _sharedPreferences.edit().putString(_getDeviceNameKey(deviceId), name).apply();
    }

    public String getOrSetDeviceName(int deviceId, @NonNull String defaultName) {
        String name = getDeviceName(deviceId);
        if (name != null) return name;

        this.setDeviceName(deviceId, defaultName);
        return defaultName;
    }

    public @Nullable String getDeviceDescription(int deviceId) {
        return _sharedPreferences.getString(_getDeviceDescriptionKey(deviceId), null);
    }

    public void setDeviceDescription(int deviceId, @NonNull String description) {
        _sharedPreferences.edit().putString(_getDeviceDescriptionKey(deviceId), description).apply();
    }

    public String getOrSetDeviceDescription(int deviceId, @NonNull String defaultDescription) {
        String description = getDeviceDescription(deviceId);
        if (description != null) return description;

        this.setDeviceDescription(deviceId, defaultDescription);
        return defaultDescription;
    }

    public @Nullable Integer getDeviceIconId(int deviceId) {
        int iconId = _sharedPreferences.getInt(_getDeviceIconIdKey(deviceId), -1);
        return iconId != -1 ? iconId : null;
    }

    public void setDeviceIconId(int deviceId, @DrawableRes int iconId) {
        _sharedPreferences.edit().putInt(_getDeviceIconIdKey(deviceId), iconId).apply();
    }

    public int getOrSetDeviceIconId(int deviceId, @DrawableRes int defaultIconId) {
        Integer iconId = getDeviceIconId(deviceId);
        if (iconId != null) return iconId;

        this.setDeviceIconId(deviceId, defaultIconId);
        return defaultIconId;
    }
}
