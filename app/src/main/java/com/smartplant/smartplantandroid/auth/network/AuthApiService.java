package com.smartplant.smartplantandroid.auth.network;

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
import com.smartplant.smartplantandroid.utils.network.http.excpetions.BadResponseException;
import com.smartplant.smartplantandroid.utils.network.http.excpetions.HttpTransferResponseException;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AuthApiService {
    private final Gson gson = new Gson();

    private final String apiAuthBase;
    private final String apiUsersBase;

    public AuthApiService() {
        // Initializing here because settings will be loaded only after Application.onCreate performed
        this.apiAuthBase = HTTPApiHelper.getBaseURL("http") + "auth/";
        this.apiUsersBase = HTTPApiHelper.getBaseURL("http") + "users/";
    }

    private Pair<Response, TransferResponse> sendRequest(Request request) throws AuthFailedException {
        try {
            return HTTPApiHelper.sendRequest(request);
        } catch (BadResponseException e) {
            AppLogger.error("Got bad response while sending register request", e);
            throw new AuthFailedException(e);
        } catch (HttpTransferResponseException e) {
            TransferResponse transferResponse = e.getTransferResponse();
            AppLogger.error(e, "Received register fail (%d): %s", transferResponse.getApplicationStatusCode(), transferResponse.getMessage());
            throw new AuthFailedException(e);
        } catch (IOException e) {
            AppLogger.error("Got unknown IO error while sending register request", e);
            throw new AuthFailedException(e);
        }
    }

    public Pair<User, AuthTokenPair> register(String username, String password) throws AuthFailedException {
        JSONObject json = new JSONObject();

        try {
            json.put("username", username);
            json.put("password", password);
        } catch (Exception e) {
            AppLogger.error("Got unknown error while forming register request body", e);
            throw new AuthFailedException(e);
        }

        RequestBody body = RequestBody.create(json.toString(), MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder().url(apiAuthBase + "/register/").post(body).build();
        Pair<Response, TransferResponse> responses = this.sendRequest(request);

        assert responses.second.getData() != null;
        JsonObject userJson = responses.second.getData().getAsJsonObject("user");
        JsonObject tokensJson = responses.second.getData().getAsJsonObject("tokens");
        return new Pair<>(gson.fromJson(userJson, User.class), gson.fromJson(tokensJson, AuthTokenPair.class));
    }

    public Pair<User, AuthTokenPair> login(String username, String password) throws AuthFailedException {
        JSONObject json = new JSONObject();

        try {
            json.put("username", username);
            json.put("password", password);
        } catch (Exception e) {
            AppLogger.error("Got unknown error while forming login request body", e);
            throw new AuthFailedException(e);
        }

        RequestBody body = RequestBody.create(json.toString(), MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder().url(apiAuthBase + "/login/").post(body).build();
        Pair<Response, TransferResponse> responses = this.sendRequest(request);

        assert responses.second.getData() != null;
        JsonObject userJson = responses.second.getData().getAsJsonObject("user");
        JsonObject tokensJson = responses.second.getData().getAsJsonObject("tokens");
        return new Pair<>(gson.fromJson(userJson, User.class), gson.fromJson(tokensJson, AuthTokenPair.class));
    }

    public AuthTokenPair refresh(String refreshToken) throws AuthFailedException {
        JSONObject json = new JSONObject();

        try {
            json.put("refresh_token", refreshToken);
        } catch (Exception e) {
            AppLogger.error("Got unknown error while forming refresh request body", e);
            throw new AuthFailedException(e);
        }

        RequestBody body = RequestBody.create(json.toString(), MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder().url(apiAuthBase + "/refresh/").post(body).build();
        Pair<Response, TransferResponse> responses = this.sendRequest(request);

        assert responses.second.getData() != null;
        JsonObject tokensJson = responses.second.getData().getAsJsonObject("tokens");
        return gson.fromJson(tokensJson, AuthTokenPair.class);
    }

    public User getMe() throws AuthFailedException, UnauthorizedException {
        Request request = HTTPApiHelper.getAuthorizedRequestBuilder().url(apiUsersBase + "/me/").build();
        Pair<Response, TransferResponse> responses = this.sendRequest(request);

        assert responses.second.getData() != null;
        JsonObject userJson = responses.second.getData().getAsJsonObject("user");
        return gson.fromJson(userJson, User.class);
    }
}
