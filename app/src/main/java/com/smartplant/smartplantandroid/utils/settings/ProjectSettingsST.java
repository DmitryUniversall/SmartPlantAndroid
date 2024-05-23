package com.smartplant.smartplantandroid.utils.settings;

import android.content.Context;
import android.content.SharedPreferences;


public class ProjectSettingsST {  // TODO: Make SettingsBase class for other settings
    // Preferences
    protected static SharedPreferences _sharedPreferences;
    protected static final String _KEY_FIRST_LOAD = "IS_FIRST_LOAD";
    protected static final String _PREFERENCES_FILE_NAME = "SmartPlantPreferences";

    // Keys
    protected static final String KEY_TS_HOST = "transfer_server_host";
    protected static final String KEY_TS_PORT = "transfer_server_port";
    protected static final String KEY_TS_APP_DEVICE_ID = "app_device_id";
    protected static final String KEY_TS_API_BASE_URL = "api_base_url";
    protected static final String KEY_TS_AUTH_TOKEN_TYPE = "auth_token_type";

    // Defaults
    protected static final String DEFAULT_TS_HOST = "172.27.11.5";
    protected static final int DEFAULT_TS_PORT = 8000;
    protected static final String DEFAULT_TS_APP_DEVICE_ID = "SmartPlantAndroid";
    protected static final String DEFAULT_TS_API_BASE_URL = "/api/";
    protected static final String DEFAULT_TS_AUTH_TOKEN_TYPE = "Bearer";

    protected String TSHost;
    protected int TSPort;
    protected String TSAppDeviceId;
    protected String TSApiBaseUrl;
    protected String TSAuthTokenType;

    // Singleton
    protected static ProjectSettingsST _instance;

    public static synchronized void createInstance(Context context) {
        if (_instance != null)
            throw new RuntimeException("Project settings has already been initialized");
        _instance = new ProjectSettingsST(context);
    }

    public static synchronized ProjectSettingsST getInstance() {
        if (_instance == null)
            throw new RuntimeException("Project settings has not been initialized");
        return _instance;
    }

    protected ProjectSettingsST(Context context) {
        _sharedPreferences = context.getSharedPreferences(_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        if (_isFirstLoad()) _setDefaultSettings();
    }

    protected boolean _isFirstLoad() {
        return true;  // _sharedPreferences.getBoolean(_KEY_FIRST_LOAD, true);
    }

    protected void _setDefaultSettings() {
        SharedPreferences.Editor editor = _sharedPreferences.edit();

        editor.putString(KEY_TS_HOST, DEFAULT_TS_HOST);
        editor.putInt(KEY_TS_PORT, DEFAULT_TS_PORT);
        editor.putString(KEY_TS_APP_DEVICE_ID, DEFAULT_TS_APP_DEVICE_ID);
        editor.putString(KEY_TS_API_BASE_URL, DEFAULT_TS_API_BASE_URL);
        editor.putString(KEY_TS_AUTH_TOKEN_TYPE, DEFAULT_TS_AUTH_TOKEN_TYPE);

        editor.putBoolean(_KEY_FIRST_LOAD, false);
        editor.apply();
    }

    public String getTSHost() {
        if (this.TSHost != null) return TSHost;
        if (!_sharedPreferences.contains(KEY_TS_HOST))
            throw new RuntimeException("TS_HOST not found");
        return _sharedPreferences.getString(KEY_TS_HOST, DEFAULT_TS_HOST);
    }

    public void setTSHost(String host) {
        this.TSHost = host;
        SharedPreferences.Editor editor = _sharedPreferences.edit();
        editor.putString(KEY_TS_HOST, host);
        editor.apply();
    }

    public int getTSPort() {
        if (this.TSPort != 0) return TSPort;
        if (!_sharedPreferences.contains(KEY_TS_PORT))
            throw new RuntimeException("TS_PORT not found");
        return _sharedPreferences.getInt(KEY_TS_PORT, DEFAULT_TS_PORT);
    }

    public void setTSPort(int port) {
        this.TSPort = port;
        SharedPreferences.Editor editor = _sharedPreferences.edit();
        editor.putInt(KEY_TS_PORT, port);
        editor.apply();
    }

    public String getTSAppDeviceID() {
        if (this.TSAppDeviceId != null) return TSAppDeviceId;
        if (!_sharedPreferences.contains(KEY_TS_APP_DEVICE_ID))
            throw new RuntimeException("TS_APP_DEVICE_ID not found");
        return _sharedPreferences.getString(KEY_TS_APP_DEVICE_ID, DEFAULT_TS_APP_DEVICE_ID);
    }

    public void setTSAppDeviceID(String deviceID) {
        this.TSAppDeviceId = deviceID;
        SharedPreferences.Editor editor = _sharedPreferences.edit();
        editor.putString(KEY_TS_APP_DEVICE_ID, deviceID);
        editor.apply();
    }

    public String getApiBaseUrl() {
        if (this.TSApiBaseUrl != null) return TSApiBaseUrl;
        if (!_sharedPreferences.contains(KEY_TS_API_BASE_URL))
            throw new RuntimeException("KEY_TS_API_BASE_URL not found");
        return _sharedPreferences.getString(KEY_TS_API_BASE_URL, DEFAULT_TS_API_BASE_URL);
    }

    public void setApiBaseUrl(String apiBaseUrl) {
        this.TSApiBaseUrl = apiBaseUrl;
        SharedPreferences.Editor editor = _sharedPreferences.edit();
        editor.putString(KEY_TS_API_BASE_URL, apiBaseUrl);
        editor.apply();
    }

    public String getAuthTokenType() {
        if (this.TSAuthTokenType != null) return TSAuthTokenType;
        if (!_sharedPreferences.contains(KEY_TS_AUTH_TOKEN_TYPE))
            throw new RuntimeException("KEY_TS_AUTH_TOKEN_TYPE not found");
        return _sharedPreferences.getString(KEY_TS_AUTH_TOKEN_TYPE, DEFAULT_TS_AUTH_TOKEN_TYPE);
    }

    public void setAuthTokenType(String authTokenType) {
        this.TSAuthTokenType = authTokenType;
        SharedPreferences.Editor editor = _sharedPreferences.edit();
        editor.putString(KEY_TS_AUTH_TOKEN_TYPE, authTokenType);
        editor.apply();
    }
}
