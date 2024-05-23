package com.smartplant.smartplantandroid.auth.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.smartplant.smartplantandroid.auth.models.AuthTokenPair;
import com.smartplant.smartplantandroid.auth.utils.TokenManager;

public class AuthRepositoryST {
    private static @Nullable AuthRepositoryST instance;
    private final @NonNull TokenManager tokenManager;

    public static synchronized void createInstance(@NonNull Context context) {
        if (instance != null)
            throw new RuntimeException("AuthRepositoryST has already been initialized");
        instance = new AuthRepositoryST(context);
    }

    public static synchronized @NonNull AuthRepositoryST getInstance() {
        if (instance == null)
            throw new RuntimeException("AuthRepositoryST has not been initialized");
        return instance;
    }

    private AuthRepositoryST(@NonNull Context context) {
        this.tokenManager = new TokenManager(context);
    }

    public boolean hasTokens() {
        return tokenManager.hasTokens();
    }

    public boolean isAuthenticated() {
        return hasTokens();
    }

    public @Nullable AuthTokenPair getTokenPair() {
        return tokenManager.getAuthTokenPair();
    }

    public void logout() {
        tokenManager.clear();
    }

//    public User register() {
//    }
//
//    public User login() {
//    }
//
//    public User getMe() {
//    }
//
//    public void refresh() {
//
//    }
}
