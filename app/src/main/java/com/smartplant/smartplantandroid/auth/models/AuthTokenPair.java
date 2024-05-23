package com.smartplant.smartplantandroid.auth.models;

public class AuthTokenPair {
    private final String _accessToken;
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
