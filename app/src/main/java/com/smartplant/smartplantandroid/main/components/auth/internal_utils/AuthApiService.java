package com.smartplant.smartplantandroid.main.components.auth.internal_utils;

import static com.smartplant.smartplantandroid.core.data.json.JsonUtils.getGson;

import android.util.Pair;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.smartplant.smartplantandroid.core.exceptions.http.BadResponseException;
import com.smartplant.smartplantandroid.core.exceptions.http.HttpApplicationResponseException;
import com.smartplant.smartplantandroid.core.logs.AppLogger;
import com.smartplant.smartplantandroid.core.models.ApplicationResponse;
import com.smartplant.smartplantandroid.core.network.ApiUtils;
import com.smartplant.smartplantandroid.core.network.http.http_api_request.HTTPApiRequest;
import com.smartplant.smartplantandroid.core.network.http.http_api_request.HTTPApiResponseProcessor;
import com.smartplant.smartplantandroid.main.components.auth.exceptions.AuthFailedException;
import com.smartplant.smartplantandroid.main.components.auth.models.AuthTokenPair;
import com.smartplant.smartplantandroid.main.components.auth.models.User;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.RequestBody;

public class AuthApiService {
    private static final Gson _gson = getGson();

    private final String _authApiBase;

    public AuthApiService() {
        // Initializing here because settings will be loaded only after Application.onCreate is performed
        this._authApiBase = ApiUtils.getBaseURL("http") + "auth/";
    }

    private <T> HTTPApiRequest<T> sendAuthRequest(Request request, HTTPApiResponseProcessor<T> responseProcessor) {
        return ApiUtils.createApiRequest(request, responseProcessor).onFailure((call, error) -> {
            if (error instanceof BadResponseException) {
                AppLogger.error("Got bad response while sending request", error);
                throw new AuthFailedException(error);
            } else if (error instanceof HttpApplicationResponseException) {
                ApplicationResponse applicationResponse = ((HttpApplicationResponseException) error).getApplicationResponse();
                AppLogger.error(error, "Auth request fail (%d): %s", applicationResponse.getApplicationStatusCode(), applicationResponse.getMessage());
                throw new AuthFailedException("Auth request fail", (HttpApplicationResponseException) error);
            } else if (error instanceof IOException) {
                AppLogger.error("Got unknown IO error while sending request", error);
                throw new AuthFailedException(error);
            }
        });
    }

    public HTTPApiRequest<Pair<User, AuthTokenPair>> register(String username, String password) {
        JsonObject json = new JsonObject();
        json.addProperty("username", username);
        json.addProperty("password", password);

        RequestBody body = ApiUtils.createRequestBody(json);
        Request request = new Request.Builder().url(_authApiBase + "register/").post(body).build();

        return this.sendAuthRequest(request, ((response, applicationResponse) -> {
            assert applicationResponse.getData() != null;
            JsonObject userJson = applicationResponse.getData().getAsJsonObject("user");
            JsonObject tokensJson = applicationResponse.getData().getAsJsonObject("tokens");
            return new Pair<>(_gson.fromJson(userJson, User.class), _gson.fromJson(tokensJson, AuthTokenPair.class));
        }));
    }

    public HTTPApiRequest<Pair<User, AuthTokenPair>> login(String username, String password) {
        JsonObject json = new JsonObject();
        json.addProperty("username", username);
        json.addProperty("password", password);

        RequestBody body = ApiUtils.createRequestBody(json);
        Request request = new Request.Builder().url(_authApiBase + "login/").post(body).build();

        return this.sendAuthRequest(request, ((response, applicationResponse) -> {
            assert applicationResponse.getData() != null;
            JsonObject userJson = applicationResponse.getData().getAsJsonObject("user");
            JsonObject tokensJson = applicationResponse.getData().getAsJsonObject("tokens");
            return new Pair<>(_gson.fromJson(userJson, User.class), _gson.fromJson(tokensJson, AuthTokenPair.class));
        }));
    }

    public HTTPApiRequest<AuthTokenPair> refresh(String refreshToken) {
        JsonObject json = new JsonObject();
        json.addProperty("refresh_token", refreshToken);

        RequestBody body = ApiUtils.createRequestBody(json);
        Request request = new Request.Builder().url(_authApiBase + "refresh/").post(body).build();

        return this.sendAuthRequest(request, ((response, applicationResponse) -> {
            assert applicationResponse.getData() != null;
            JsonObject tokensJson = applicationResponse.getData().getAsJsonObject("tokens");
            return _gson.fromJson(tokensJson, AuthTokenPair.class);
        }));
    }

    public HTTPApiRequest<User> fetchMe() {
        Request request = ApiUtils.getAuthorizedRequestBuilder().url(_authApiBase + "me/").build();

        return this.sendAuthRequest(request, ((response, applicationResponse) -> {
            assert applicationResponse.getData() != null;
            JsonObject userJson = applicationResponse.getData().getAsJsonObject("user");
            return _gson.fromJson(userJson, User.class);
        }));
    }
}
