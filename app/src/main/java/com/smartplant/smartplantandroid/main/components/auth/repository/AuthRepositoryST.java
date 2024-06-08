package com.smartplant.smartplantandroid.main.components.auth.repository;

import android.content.Context;
import android.util.Pair;

import androidx.annotation.Nullable;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.smartplant.smartplantandroid.core.network.http.http_api_request.HTTPApiRequest;
import com.smartplant.smartplantandroid.main.components.auth.exceptions.UnauthorizedException;
import com.smartplant.smartplantandroid.main.components.auth.internal_utils.AuthApiService;
import com.smartplant.smartplantandroid.main.components.auth.internal_utils.AuthTokenManager;
import com.smartplant.smartplantandroid.main.components.auth.models.AuthTokenPair;
import com.smartplant.smartplantandroid.main.components.auth.models.User;

public class AuthRepositoryST {
    // Singleton
    private static @Nullable AuthRepositoryST _instance;

    // Utils
    private final AuthTokenManager _tokenManager;
    private final AuthApiService _authApiService;

    // Cache
    private boolean _isLoaded;
    private @Nullable User _currentUser;

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
        this._tokenManager = new AuthTokenManager(context);
        this._authApiService = new AuthApiService();
    }

    public boolean hasTokens() {
        return _tokenManager.hasTokens();
    }

    public boolean isAuthenticated() {
        return this.hasTokens() && this._isLoaded;
    }

    private void setCurrentUser(User user) {
        this._isLoaded = true;
        this._currentUser = user;
    }

    public @Nullable AuthTokenPair getTokenPair() {
        return _tokenManager.getAuthTokenPair();
    }

    public @Nullable User getMe() {
        return this._currentUser;
    }

    public void logout() {  // TODO: Request for logout
        _tokenManager.clear();
        _currentUser = null;
    }

    public HTTPApiRequest<Pair<User, AuthTokenPair>> register(String username, String password) {
        return this._authApiService.register(username, password).onSuccess(((result, response, applicationResponse) -> {
            this.setCurrentUser(result.first);
            _tokenManager.saveTokens(result.second);
        }));
    }

    public HTTPApiRequest<Pair<User, AuthTokenPair>> login(String username, String password) {
        return this._authApiService.login(username, password).onSuccess(((result, response, applicationResponse) -> {
            this.setCurrentUser(result.first);
            _tokenManager.saveTokens(result.second);
        }));
    }

    public HTTPApiRequest<AuthTokenPair> refresh() throws UnauthorizedException {
        AuthTokenPair currentTokenPair = this.getTokenPair();
        if (currentTokenPair == null)
            throw new UnauthorizedException("Unable to refresh tokens: unauthorized");
        return this._authApiService.refresh(currentTokenPair.getRefreshToken()).onSuccess((result, response, applicationResponse) -> _tokenManager.saveTokens(result));
    }

    @CanIgnoreReturnValue
    public HTTPApiRequest<User> fetchMe() throws UnauthorizedException {
        return this._authApiService.fetchMe().onSuccess(((result, response, applicationResponse) -> this.setCurrentUser(result)));
    }
}
