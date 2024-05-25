package com.smartplant.smartplantandroid.auth.repository;

import android.content.Context;
import android.util.Pair;

import androidx.annotation.Nullable;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.smartplant.smartplantandroid.auth.exceptions.UnauthorizedException;
import com.smartplant.smartplantandroid.auth.models.AuthTokenPair;
import com.smartplant.smartplantandroid.auth.models.User;
import com.smartplant.smartplantandroid.auth.network.AuthApiService;
import com.smartplant.smartplantandroid.auth.utils.TokenManager;
import com.smartplant.smartplantandroid.utils.network.http.api_request.ApiHttpRequest;

public class AuthRepositoryST {
    private static @Nullable AuthRepositoryST _instance;

    private final TokenManager _tokenManager;
    private final AuthApiService _authApiService;
    private User _currentUser;

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
        this._tokenManager = new TokenManager(context);
        this._authApiService = new AuthApiService();
    }

    public boolean hasTokens() {
        return _tokenManager.hasTokens();
    }

    public @Nullable AuthTokenPair getTokenPair() {
        return _tokenManager.getAuthTokenPair();
    }

    public void logout() {
        _tokenManager.clear();
        _currentUser = null;
    }

    public ApiHttpRequest<Pair<User, AuthTokenPair>> register(String username, String password) {
        return this._authApiService.register(username, password).onSuccess(((result, response, transferResponse) -> _tokenManager.saveTokens(result.second)));
    }

    public ApiHttpRequest<Pair<User, AuthTokenPair>> login(String username, String password) {
        return this._authApiService.login(username, password).onSuccess(((result, response, transferResponse) -> _tokenManager.saveTokens(result.second)));
    }

    public ApiHttpRequest<AuthTokenPair> refresh() throws UnauthorizedException {
        AuthTokenPair currentTokenPair = this.getTokenPair();
        if (currentTokenPair == null)
            throw new UnauthorizedException("Unable to refresh tokens: unauthorized");
        return this._authApiService.refresh(currentTokenPair.getRefreshToken()).onSuccess((result, response, transferResponse) -> _tokenManager.saveTokens(result));
    }

    @CanIgnoreReturnValue
    public ApiHttpRequest<User> fetchMe() throws UnauthorizedException {
        return this._authApiService.getMe().onSuccess(((result, response, transferResponse) -> this._currentUser = result));
    }

    public @Nullable User getMe() {
        return this._currentUser;
    }
}
