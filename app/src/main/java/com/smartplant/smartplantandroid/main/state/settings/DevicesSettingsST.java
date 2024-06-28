package com.smartplant.smartplantandroid.main.state.settings;

import android.content.Context;
import android.content.SharedPreferences;

public class DevicesSettingsST {
    // Singleton
    private static DevicesSettingsST _instance;

    // Preferences
    private static SharedPreferences _sharedPreferences;
    private static final String _PREFERENCES_FILE_NAME = "SmartPlantPreferencesDevices";

    // Keys
    private static final String _KEY_FIRST_LOAD = "is_first_load";
    private static final String _KEY_DATA_UPDATE_INTERVAL = "data_update_interval";

    // Defaults
    private static final int _DEFAULT_DATA_UPDATE_INTERVAL = 5000;

    // Cache
    private int _dataUpdateInterval;

    public static synchronized void createInstance(Context context) {
        if (_instance != null)
            throw new RuntimeException("Project settings has already been initialized");
        _instance = new DevicesSettingsST(context);
    }

    public static synchronized DevicesSettingsST getInstance() {
        if (_instance == null)
            throw new RuntimeException("Project settings has not been initialized");
        return _instance;
    }

    private DevicesSettingsST(Context context) {
        _sharedPreferences = context.getSharedPreferences(_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        if (_isFirstLoad()) _setDefaultSettings();
    }

    private boolean _isFirstLoad() {
        return _sharedPreferences.getBoolean(_KEY_FIRST_LOAD, true);
    }

    private void _setDefaultSettings() {
        SharedPreferences.Editor editor = _sharedPreferences.edit();

        editor.putInt(_KEY_DATA_UPDATE_INTERVAL, _DEFAULT_DATA_UPDATE_INTERVAL);

        editor.putBoolean(_KEY_FIRST_LOAD, false);
        editor.apply();
    }

    public int getDataUpdateInterval() {
        if (this._dataUpdateInterval != 0) return _dataUpdateInterval;
        return _sharedPreferences.getInt(_KEY_DATA_UPDATE_INTERVAL, _DEFAULT_DATA_UPDATE_INTERVAL);
    }

    public void setDataUpdateInterval(int dataUpdateInterval) {
        this._dataUpdateInterval = dataUpdateInterval;
        _sharedPreferences.edit().putInt(_KEY_DATA_UPDATE_INTERVAL, dataUpdateInterval).apply();
    }
}
