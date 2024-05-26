package com.smartplant.smartplantandroid.utils.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.smartplant.smartplantandroid.auth.exceptions.UnauthorizedException;
import com.smartplant.smartplantandroid.auth.models.AuthTokenPair;
import com.smartplant.smartplantandroid.auth.repository.AuthRepositoryST;
import com.smartplant.smartplantandroid.utils.network.http.api_request.ApiHttpRequest;
import com.smartplant.smartplantandroid.utils.network.http.api_request.ApiHttpResponseProcessor;
import com.smartplant.smartplantandroid.utils.settings.ProjectSettingsST;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class ApiHelper {
    private static final Gson _gson = createGson();
    private static final OkHttpClient _client = createOkHttpClient();

    private static Gson createGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
        return gsonBuilder.create();
    }

    private static OkHttpClient createOkHttpClient() {
        return new OkHttpClient();
    }

    public static Gson getGson() {
        return _gson;
    }

    public static OkHttpClient getOkHttpClient() {
        return _client;
    }

    public static Request.Builder getAuthorizedRequestBuilder() throws UnauthorizedException {
        ProjectSettingsST projectSettings = ProjectSettingsST.getInstance();
        AuthRepositoryST authRepository = AuthRepositoryST.getInstance();

        if (!authRepository.hasTokens())
            throw new UnauthorizedException("Cannot get authorized request builder: Unauthorized");

        AuthTokenPair tokenPair = authRepository.getTokenPair();
        assert tokenPair != null;
        return new Request.Builder().header("Authorization", projectSettings.getAuthTokenType() + " " + tokenPair.getAccessToken());
    }

    public static Request.Builder getAuthorizedRequestBuilder(Request request) throws UnauthorizedException {
        ProjectSettingsST projectSettings = ProjectSettingsST.getInstance();
        AuthRepositoryST authRepository = AuthRepositoryST.getInstance();

        if (!authRepository.hasTokens())
            throw new UnauthorizedException("Cannot get authorized request builder: Unauthorized");

        AuthTokenPair tokenPair = authRepository.getTokenPair();
        assert tokenPair != null;
        return request.newBuilder().header("Authorization", projectSettings.getAuthTokenType() + " " + tokenPair.getAccessToken());
    }

    public static String getBaseURL(String protocol) {
        ProjectSettingsST projectSettings = ProjectSettingsST.getInstance();

        return protocol +
                "://" +
                projectSettings.getTSHost() +
                ":" +
                projectSettings.getTSPort() +
                projectSettings.getApiBaseUrl();
    }

    public static <T> ApiHttpRequest<T> createApiRequest(Request request, ApiHttpResponseProcessor<T> responseProcessor) {
        return new ApiHttpRequest<>(request, responseProcessor, _gson, _client);
    }
}
