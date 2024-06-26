package com.smartplant.smartplantandroid.main.state.settings;

import android.content.Context;
import android.content.SharedPreferences;

public class NetworkSettingsST {
    // Singleton
    protected static NetworkSettingsST _instance;

    // Preferences
    protected static SharedPreferences _sharedPreferences;
    protected static final String _PREFERENCES_FILE_NAME = "SmartPlantPreferencesNetwork";

    // Keys
    protected static final String _KEY_FIRST_LOAD = "is_first_load";
    protected static final String _KEY_TS_HOST = "transfer_server_host";
    protected static final String _KEY_TS_PORT = "transfer_server_port";
    protected static final String _KEY_TS_API_BASE_URL = "api_base_url";
    protected static final String _KEY_TS_AUTH_TOKEN_TYPE = "auth_token_type";

    // Defaults
    protected static final int _DEFAULT_TS_PORT = 8000;
    protected static final String _DEFAULT_TS_HOST = "192.168.1.38";
    protected static final String _DEFAULT_TS_API_BASE_URL = "/api/";
    protected static final String _DEFAULT_TS_AUTH_TOKEN_TYPE = "Bearer";

    // Cache
    protected int TSPort;
    protected String TSHost;
    protected String TSApiBaseUrl;
    protected String TSAuthTokenType;

    public static synchronized void createInstance(Context context) {
        if (_instance != null)
            throw new RuntimeException("Project settings has already been initialized");
        _instance = new NetworkSettingsST(context);
    }

    public static synchronized NetworkSettingsST getInstance() {
        if (_instance == null)
            throw new RuntimeException("Project settings has not been initialized");
        return _instance;
    }

    protected NetworkSettingsST(Context context) {
        _sharedPreferences = context.getSharedPreferences(_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        if (_isFirstLoad()) _setDefaultSettings();
    }

    protected boolean _isFirstLoad() {
        return _sharedPreferences.getBoolean(_KEY_FIRST_LOAD, true);
    }

    protected void _setDefaultSettings() {
        SharedPreferences.Editor editor = _sharedPreferences.edit();

        editor.putInt(_KEY_TS_PORT, _DEFAULT_TS_PORT);
        editor.putString(_KEY_TS_HOST, _DEFAULT_TS_HOST);
        editor.putString(_KEY_TS_API_BASE_URL, _DEFAULT_TS_API_BASE_URL);
        editor.putString(_KEY_TS_AUTH_TOKEN_TYPE, _DEFAULT_TS_AUTH_TOKEN_TYPE);

        editor.putBoolean(_KEY_FIRST_LOAD, false);
        editor.apply();
    }

    public String getTSHost() {
        if (this.TSHost != null) return TSHost;
        return _sharedPreferences.getString(_KEY_TS_HOST, _DEFAULT_TS_HOST);
    }

    public void setTSHost(String host) {
        this.TSHost = host;
        _sharedPreferences.edit().putString(_KEY_TS_HOST, host).apply();
    }

    public int getTSPort() {
        if (this.TSPort != 0) return TSPort;
        return _sharedPreferences.getInt(_KEY_TS_PORT, _DEFAULT_TS_PORT);
    }

    public void setTSPort(int port) {
        this.TSPort = port;
        _sharedPreferences.edit().putInt(_KEY_TS_PORT, port).apply();
    }

    public String getApiBaseUrl() {
        if (this.TSApiBaseUrl != null) return TSApiBaseUrl;
        return _sharedPreferences.getString(_KEY_TS_API_BASE_URL, _DEFAULT_TS_API_BASE_URL);
    }

    public void setApiBaseUrl(String apiBaseUrl) {
        this.TSApiBaseUrl = apiBaseUrl;
        _sharedPreferences.edit().putString(_KEY_TS_API_BASE_URL, apiBaseUrl).apply();
    }

    public String getAuthTokenType() {
        if (this.TSAuthTokenType != null) return TSAuthTokenType;
        return _sharedPreferences.getString(_KEY_TS_AUTH_TOKEN_TYPE, _DEFAULT_TS_AUTH_TOKEN_TYPE);
    }

    public void setAuthTokenType(String authTokenType) {
        this.TSAuthTokenType = authTokenType;
        _sharedPreferences.edit().putString(_KEY_TS_AUTH_TOKEN_TYPE, authTokenType).apply();
    }
}
