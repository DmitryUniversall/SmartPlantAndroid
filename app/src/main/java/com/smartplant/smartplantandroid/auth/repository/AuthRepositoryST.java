package com.smartplant.smartplantandroid.auth.repository;

import android.content.Context;
import android.util.Pair;

import androidx.annotation.Nullable;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.smartplant.smartplantandroid.auth.exceptions.AuthFailedException;
import com.smartplant.smartplantandroid.auth.exceptions.UnauthorizedException;
import com.smartplant.smartplantandroid.auth.models.AuthTokenPair;
import com.smartplant.smartplantandroid.auth.models.User;
import com.smartplant.smartplantandroid.auth.network.AuthApiService;
import com.smartplant.smartplantandroid.auth.utils.TokenManager;
import com.smartplant.smartplantandroid.utils.network.http.api_request.ApiHttpRequest;

public class AuthRepositoryST {
    private static @Nullable AuthRepositoryST _instance;

    private final TokenManager tokenManager;
    private final AuthApiService authApiService;
    private User currentUser;

    public static synchronized void createInstance(Context context) {
        if (_instance != null)
            throw new RuntimeException("AuthRepositoryST has already been initialized");
        _instance = new AuthRepositoryST(context);
    }

    public static synchronized AuthRepositoryST getInstance() {
        if (_instance == null)
            throw new RuntimeException("AuthRepositoryST has not been initialized");
        return _instance;
    }

    private AuthRepositoryST(Context context) {
        this.tokenManager = new TokenManager(context);
        this.authApiService = new AuthApiService();
    }

    public boolean hasTokens() {
        return tokenManager.hasTokens();
    }

    public @Nullable AuthTokenPair getTokenPair() {
        return tokenManager.getAuthTokenPair();
    }

    public void logout() {
        tokenManager.clear();
        currentUser = null;
    }

    public ApiHttpRequest<Pair<User, AuthTokenPair>> register(String username, String password) throws AuthFailedException {
        return this.authApiService.register(username, password).onSuccess((result -> tokenManager.saveTokens(result.second)));
    }

    public ApiHttpRequest<Pair<User, AuthTokenPair>> login(String username, String password) throws AuthFailedException {
        return this.authApiService.login(username, password).onSuccess((result -> tokenManager.saveTokens(result.second)));
    }

    public ApiHttpRequest<AuthTokenPair> refresh() throws UnauthorizedException {
        AuthTokenPair currentTokenPair = this.getTokenPair();
        if (currentTokenPair == null)
            throw new UnauthorizedException("Unable to refresh tokens: unauthorized");
        return this.authApiService.refresh(currentTokenPair.getRefreshToken()).onSuccess((tokenManager::saveTokens));
    }

    @CanIgnoreReturnValue
    public ApiHttpRequest<User> fetchMe() throws UnauthorizedException {
        return this.authApiService.getMe().onSuccess((result -> this.currentUser = result));
    }

    public @Nullable User getMe() {
        return this.currentUser;
    }
}
