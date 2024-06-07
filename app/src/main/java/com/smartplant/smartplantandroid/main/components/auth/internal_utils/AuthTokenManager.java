package com.smartplant.smartplantandroid.main.components.auth.internal_utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.smartplant.smartplantandroid.main.components.auth.models.AuthTokenPair;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class AuthTokenManager {
    // Preferences
    private static final String _PREFERENCES_FILENAME = "authData";
    private final SharedPreferences _preferences;

    // Keys
    private static final String _KEY_ACCESS_TOKEN = "accessToken";
    private static final String _KEY_REFRESH_TOKEN = "refreshToken";

    // Cache
    private AuthTokenPair _tokenPair;

    public AuthTokenManager(Context context) {
        this._preferences = this._createEncryptedPreferences(context);
    }

    private SharedPreferences _createEncryptedPreferences(Context ctx) {
        try {
            return EncryptedSharedPreferences.create(
                    _PREFERENCES_FILENAME,
                    MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
                    ctx,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void clear() {
        _preferences.edit().clear().apply();
    }

    public boolean hasTokens() {
        return this.getAuthTokenPair() != null;
    }

    public void saveTokens(AuthTokenPair tokenPair) {
        this._tokenPair = tokenPair;
        _preferences.edit()
                .putString(_KEY_ACCESS_TOKEN, tokenPair.getAccessToken())
                .putString(_KEY_REFRESH_TOKEN, tokenPair.getRefreshToken())
                .apply();
    }

    public @Nullable AuthTokenPair getAuthTokenPair() {
        if (this._tokenPair != null) return this._tokenPair;

        String accessToken = _preferences.getString(_KEY_ACCESS_TOKEN, null);
        String refreshToken = _preferences.getString(_KEY_REFRESH_TOKEN, null);
        this._tokenPair = (accessToken != null && refreshToken != null) ? new AuthTokenPair(accessToken, refreshToken) : null;
        return this._tokenPair;
    }
}
