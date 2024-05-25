package com.smartplant.smartplantandroid.auth.network;

import static com.smartplant.smartplantandroid.utils.data.json.JsonUtils.getGson;

import android.util.Pair;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.smartplant.smartplantandroid.auth.exceptions.AuthFailedException;
import com.smartplant.smartplantandroid.auth.exceptions.UnauthorizedException;
import com.smartplant.smartplantandroid.auth.models.AuthTokenPair;
import com.smartplant.smartplantandroid.auth.models.User;
import com.smartplant.smartplantandroid.utils.AppLogger;
import com.smartplant.smartplantandroid.utils.network.TransferResponse;
import com.smartplant.smartplantandroid.utils.network.http.HTTPApiHelper;
import com.smartplant.smartplantandroid.utils.network.http.api_request.ApiHttpRequest;
import com.smartplant.smartplantandroid.utils.network.http.api_request.ApiHttpResponseProcessor;
import com.smartplant.smartplantandroid.utils.network.http.excpetions.BadResponseException;
import com.smartplant.smartplantandroid.utils.network.http.excpetions.HttpTransferResponseException;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;


public class AuthApiService {
    private final Gson gson = getGson();

    private final String apiAuthBase;
    private final String apiUsersBase;

    public AuthApiService() {
        // Initializing here because settings will be loaded only after Application.onCreate performed
        this.apiAuthBase = HTTPApiHelper.getBaseURL("http") + "auth/";
        this.apiUsersBase = HTTPApiHelper.getBaseURL("http") + "users/";
    }

    private <T> ApiHttpRequest<T> sendRequest(Request request, ApiHttpResponseProcessor<T> responseProcessor) {
        return HTTPApiHelper.createApiRequest(request, responseProcessor).onFailure((call, error) -> {
            if (error instanceof BadResponseException) {
                AppLogger.error("Got bad response while sending request", error);
                throw new AuthFailedException(error);
            } else if (error instanceof HttpTransferResponseException) {
                TransferResponse transferResponse = ((HttpTransferResponseException) error).getTransferResponse();
                AppLogger.error(error, "Received register fail (%d): %s", transferResponse.getApplicationStatusCode(), transferResponse.getMessage());
                throw new AuthFailedException(error);
            } else if (error instanceof IOException) {
                AppLogger.error("Got unknown IO error while sending register request", error);
                throw new AuthFailedException(error);
            }
        });
    }

    public ApiHttpRequest<Pair<User, AuthTokenPair>> register(String username, String password) {
        JSONObject json = new JSONObject();

        try {
            json.put("username", username);
            json.put("password", password);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        RequestBody body = RequestBody.create(json.toString(), MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder().url(apiAuthBase + "register/").post(body).build();

        return this.sendRequest(request, ((response, transferResponse) -> {
            assert transferResponse.getData() != null;
            JsonObject userJson = transferResponse.getData().getAsJsonObject("user");
            JsonObject tokensJson = transferResponse.getData().getAsJsonObject("tokens");
            return new Pair<>(gson.fromJson(userJson, User.class), gson.fromJson(tokensJson, AuthTokenPair.class));
        }));
    }

    public ApiHttpRequest<Pair<User, AuthTokenPair>> login(String username, String password) {
        JSONObject json = new JSONObject();

        try {
            json.put("username", username);
            json.put("password", password);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        RequestBody body = RequestBody.create(json.toString(), MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder().url(apiAuthBase + "login/").post(body).build();

        return this.sendRequest(request, ((response, transferResponse) -> {
            assert transferResponse.getData() != null;
            JsonObject userJson = transferResponse.getData().getAsJsonObject("user");
            JsonObject tokensJson = transferResponse.getData().getAsJsonObject("tokens");
            return new Pair<>(gson.fromJson(userJson, User.class), gson.fromJson(tokensJson, AuthTokenPair.class));
        }));
    }

    public ApiHttpRequest<AuthTokenPair> refresh(String refreshToken) {
        JSONObject json = new JSONObject();

        try {
            json.put("refresh_token", refreshToken);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        RequestBody body = RequestBody.create(json.toString(), MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder().url(apiAuthBase + "refresh/").post(body).build();

        return this.sendRequest(request, ((response, transferResponse) -> {
            assert transferResponse.getData() != null;
            JsonObject tokensJson = transferResponse.getData().getAsJsonObject("tokens");
            return gson.fromJson(tokensJson, AuthTokenPair.class);
        }));
    }

    public ApiHttpRequest<User> getMe() throws UnauthorizedException {
        Request request = HTTPApiHelper.getAuthorizedRequestBuilder().url(apiUsersBase + "me/").build();

        return this.sendRequest(request, ((response, transferResponse) -> {
            assert transferResponse.getData() != null;
            JsonObject userJson = transferResponse.getData().getAsJsonObject("user");
            return gson.fromJson(userJson, User.class);
        }));
    }
}
