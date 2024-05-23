package com.smartplant.smartplantandroid.auth.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.smartplant.smartplantandroid.auth.models.AuthTokenPair;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class TokenManager {
    private static final @NonNull String _PREFERENCES_FILENAME = "authPrefs";
    private static final @NonNull String _KEY_ACCESS_TOKEN = "accessToken";
    private static final @NonNull String _KEY_REFRESH_TOKEN = "refreshToken";

    private final @NonNull Context _context;
    private final @NonNull SharedPreferences _preferences;

    public TokenManager(@NonNull Context context) {
        this._context = context;
        this._preferences = this.createEncryptedPreferences(context);
    }

    private @NonNull SharedPreferences createEncryptedPreferences(Context ctx) {
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
        SharedPreferences.Editor editor = _preferences.edit();
        editor.clear();
        editor.apply();
    }

    public boolean hasTokens() {
        return this.getAuthTokenPair() != null;
    }

    public void saveTokens(@NonNull AuthTokenPair tokenPair) {
        SharedPreferences.Editor editor = _preferences.edit();
        editor.putString(_KEY_ACCESS_TOKEN, tokenPair.getAccessToken());
        editor.putString(_KEY_REFRESH_TOKEN, tokenPair.getRefreshToken());
        editor.apply();
    }

    public @Nullable AuthTokenPair getAuthTokenPair() {
        String accessToken = _preferences.getString(_KEY_ACCESS_TOKEN, null);
        String refreshToken = _preferences.getString(_KEY_REFRESH_TOKEN, null);
        return (accessToken != null && refreshToken != null) ? new AuthTokenPair(accessToken, refreshToken) : null;
    }

    public @NonNull Context getContext() {
        return _context;
    }
}
