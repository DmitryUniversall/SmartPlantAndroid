package com.smartplant.smartplantandroid.main.components.devices.utils;

import android.content.Context;
import android.content.SharedPreferences;

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

    public @Nullable String getDeviceDescription(int deviceId) {
        return _sharedPreferences.getString(_getDeviceDescriptionKey(deviceId), null);
    }

    public @Nullable Integer getDeviceIconId(int deviceId) {
        int iconId = _sharedPreferences.getInt(_getDeviceIconIdKey(deviceId), -1);
        return iconId != -1 ? iconId : null;
    }
}
