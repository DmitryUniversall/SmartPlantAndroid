package com.smartplant.smartplantandroid.auth.models;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class AuthTokenPair {
    @SerializedName("access_token")
    private final @NonNull String _accessToken;

    @SerializedName("refresh_token")
    private final @NonNull String _refreshToken;

    public AuthTokenPair(@NonNull String accessToken, @NonNull String refreshToken) {
        this._accessToken = accessToken;
        this._refreshToken = refreshToken;
    }

    @NonNull
    public String getAccessToken() {
        return _accessToken;
    }

    @NonNull
    public String getRefreshToken() {
        return _refreshToken;
    }
}
