package com.smartplant.smartplantandroid.core.network;

import com.google.gson.JsonObject;
import com.smartplant.smartplantandroid.core.network.http.http_api_request.HTTPApiRequest;
import com.smartplant.smartplantandroid.core.network.http.http_api_request.HTTPApiResponseProcessor;
import com.smartplant.smartplantandroid.main.state.settings.NetworkSettingsST;
import com.smartplant.smartplantandroid.main.components.auth.exceptions.UnauthorizedException;
import com.smartplant.smartplantandroid.main.components.auth.models.AuthTokenPair;
import com.smartplant.smartplantandroid.main.components.auth.repository.AuthRepositoryST;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ApiUtils {
    private static final OkHttpClient _client = _createOkHttpClient();

    private static OkHttpClient _createOkHttpClient() {
        return new OkHttpClient();
    }

    public static OkHttpClient getOkHttpClient() {
        return _client;
    }

    public static String getBaseURL(String protocol) {
        NetworkSettingsST projectSettings = NetworkSettingsST.getInstance();

        return protocol +
                "://" +
                projectSettings.getTSHost() +
                ":" +
                projectSettings.getTSPort() +
                projectSettings.getApiBaseUrl();
    }

    public static Request.Builder getAuthorizedRequestBuilder() throws UnauthorizedException {
        NetworkSettingsST projectSettings = NetworkSettingsST.getInstance();
        AuthRepositoryST authRepository = AuthRepositoryST.getInstance();

        if (!authRepository.hasTokens())
            throw new UnauthorizedException("Cannot get authorized request builder: Unauthorized");

        AuthTokenPair tokenPair = authRepository.getTokenPair();
        assert tokenPair != null;
        return new Request.Builder().header("Authorization", projectSettings.getAuthTokenType() + " " + tokenPair.getAccessToken());
    }

    public static Request.Builder getAuthorizedRequestBuilder(Request request) throws UnauthorizedException {
        NetworkSettingsST projectSettings = NetworkSettingsST.getInstance();
        AuthRepositoryST authRepository = AuthRepositoryST.getInstance();

        if (!authRepository.hasTokens())
            throw new UnauthorizedException("Cannot get authorized request builder: Unauthorized");

        AuthTokenPair tokenPair = authRepository.getTokenPair();
        assert tokenPair != null;
        return request.newBuilder().header("Authorization", projectSettings.getAuthTokenType() + " " + tokenPair.getAccessToken());
    }

    public static RequestBody createRequestBody(JsonObject json) {
        return RequestBody.create(json.toString(), MediaType.get("application/json; charset=utf-8"));
    }

    public static <T> HTTPApiRequest<T> createApiRequest(Request request, HTTPApiResponseProcessor<T> responseProcessor) {
        return new HTTPApiRequest<>(request, responseProcessor, null, _client);
    }
}
