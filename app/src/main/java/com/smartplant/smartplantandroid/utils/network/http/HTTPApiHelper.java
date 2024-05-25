package com.smartplant.smartplantandroid.utils.network.http;

import static com.smartplant.smartplantandroid.utils.data.json.JsonUtils.getGson;

import com.google.gson.Gson;
import com.smartplant.smartplantandroid.auth.exceptions.UnauthorizedException;
import com.smartplant.smartplantandroid.auth.models.AuthTokenPair;
import com.smartplant.smartplantandroid.auth.repository.AuthRepositoryST;
import com.smartplant.smartplantandroid.utils.network.http.api_request.ApiHttpRequest;
import com.smartplant.smartplantandroid.utils.network.http.api_request.ApiHttpResponseProcessor;
import com.smartplant.smartplantandroid.utils.settings.ProjectSettingsST;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HTTPApiHelper {
    private static final Gson gson = getGson();
    private static final OkHttpClient client = new OkHttpClient();

    public static Request.Builder getAuthorizedRequestBuilder() throws UnauthorizedException {
        ProjectSettingsST projectSettings = ProjectSettingsST.getInstance();
        AuthRepositoryST authRepository = AuthRepositoryST.getInstance();

        if (!authRepository.hasTokens())
            throw new UnauthorizedException("Cannot get authorized request builder: Unauthorized");

        AuthTokenPair tokenPair = authRepository.getTokenPair();
        assert tokenPair != null;
        return new Request.Builder().header("Authorization", projectSettings.getAuthTokenType() + " " + tokenPair.getAccessToken());
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
        return new ApiHttpRequest<>(request, responseProcessor, gson, client);
    }
}
