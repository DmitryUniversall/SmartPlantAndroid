package com.smartplant.smartplantandroid.utils.network.http;

import android.util.Pair;

import com.google.gson.Gson;
import com.smartplant.smartplantandroid.auth.exceptions.UnauthorizedException;
import com.smartplant.smartplantandroid.auth.models.AuthTokenPair;
import com.smartplant.smartplantandroid.auth.repository.AuthRepositoryST;
import com.smartplant.smartplantandroid.utils.network.TransferResponse;
import com.smartplant.smartplantandroid.utils.network.http.excpetions.BadResponseException;
import com.smartplant.smartplantandroid.utils.network.http.excpetions.HttpTransferResponseException;
import com.smartplant.smartplantandroid.utils.settings.ProjectSettingsST;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HTTPApiHelper {
    private static final Gson gson = new Gson();
    private static final OkHttpClient client = new OkHttpClient();

    public static Request.Builder getAuthorizedRequestBuilder() throws UnauthorizedException {
        ProjectSettingsST projectSettings = ProjectSettingsST.getInstance();
        AuthRepositoryST authRepository = AuthRepositoryST.getInstance();

        if (!authRepository.isAuthenticated()) throw new UnauthorizedException("Cannot get authorized request builder: Unauthorized");

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

    public static Pair<Response, TransferResponse> sendRequest(Request request) throws IOException, HttpTransferResponseException, BadResponseException {
        ApiHTTPRequest apiRequest = new ApiHTTPRequest(request, gson, client);
        return apiRequest.execute();
    }
}
