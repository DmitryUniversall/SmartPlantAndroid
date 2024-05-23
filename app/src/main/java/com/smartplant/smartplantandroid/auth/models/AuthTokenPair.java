package com.smartplant.smartplantandroid.auth.models;

import com.google.gson.annotations.SerializedName;

public class AuthTokenPair {
    @SerializedName("access_token")
    private final String _accessToken;

    @SerializedName("refresh_token")
    private final String _refreshToken;

    public AuthTokenPair(String accessToken, String refreshToken) {
        this._accessToken = accessToken;
        this._refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return _accessToken;
    }

    public String getRefreshToken() {
        return _refreshToken;
    }
}
