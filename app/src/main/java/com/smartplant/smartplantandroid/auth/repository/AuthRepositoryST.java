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

    public boolean isAuthenticated() {
        if (!hasTokens()) return false;
        try {
            fetchMe();
            return true;
        } catch (AuthFailedException e) {  // TODO: Thrown if access token is expired?
            return false;
        } catch (UnauthorizedException e) {
            return false;
        }
    }

    public @Nullable AuthTokenPair getTokenPair() {
        return tokenManager.getAuthTokenPair();
    }

    public void logout() {
        tokenManager.clear();
    }

    public User register(String username, String password) throws AuthFailedException {
        Pair<User, AuthTokenPair> data = this.authApiService.register(username, password);
        this.tokenManager.saveTokens(data.second);
        return data.first;
    }

    public User login(String username, String password) throws AuthFailedException {
        Pair<User, AuthTokenPair> data = this.authApiService.login(username, password);
        this.tokenManager.saveTokens(data.second);
        return data.first;
    }

    public void refresh() throws AuthFailedException, UnauthorizedException {
        AuthTokenPair currentTokenPair = this.getTokenPair();
        if (currentTokenPair == null)
            throw new UnauthorizedException("Unable to refresh tokens: unauthorized");

        AuthTokenPair newTokenPair = this.authApiService.refresh(currentTokenPair.getRefreshToken());
        this.tokenManager.saveTokens(newTokenPair);
    }

    @CanIgnoreReturnValue
    public User fetchMe() throws AuthFailedException, UnauthorizedException {
        this.currentUser = this.authApiService.getMe();
        return this.currentUser;
    }

    public User getMe() throws AuthFailedException, UnauthorizedException {
        if (this.currentUser == null) return fetchMe();
        return this.currentUser;
    }
}
