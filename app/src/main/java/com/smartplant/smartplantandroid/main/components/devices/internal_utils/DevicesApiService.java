package com.smartplant.smartplantandroid.main.components.devices.internal_utils;

import static com.smartplant.smartplantandroid.core.data.json.JsonUtils.getGson;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.smartplant.smartplantandroid.core.network.ApiUtils;
import com.smartplant.smartplantandroid.core.network.http.http_api_request.HTTPApiRequest;
import com.smartplant.smartplantandroid.main.components.auth.models.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import okhttp3.Request;

public class DevicesApiService {
    private static final Gson _gson = getGson();

    private final String _devicesApiBase;

    public DevicesApiService() {
        // Initializing here because settings will be loaded only after Application.onCreate is performed
        this._devicesApiBase = ApiUtils.getBaseURL("http") + "devices/";
    }

    public HTTPApiRequest<Map<Integer, User>> fetchMyDevices() {
        Request request = ApiUtils.getAuthorizedRequestBuilder().url(_devicesApiBase + "/").build();

        return ApiUtils.createApiRequest(request, ((response, applicationResponse) -> {
            assert applicationResponse.getData() != null;
            JsonArray devicesJson = applicationResponse.getData().getAsJsonArray("devices");
            Map<Integer, User> resultMap = new HashMap<>();

            for (JsonElement deviceJson : devicesJson) {
                User device = _gson.fromJson(deviceJson.getAsJsonObject(), User.class);
                resultMap.put(device.getId(), device);
            }

            return resultMap;
        }));
    }

    public HTTPApiRequest<Optional<User>> pairDevice(String deviceUsername) {
        Request request = ApiUtils.getAuthorizedRequestBuilder().url(_devicesApiBase + "/device/" + deviceUsername + "/pair/").build();

        return ApiUtils.createApiRequest(request, ((response, applicationResponse) -> {
            if (applicationResponse.getData() == null) return Optional.empty();

            JsonArray deviceJson = applicationResponse.getData().getAsJsonArray("device_user");
            User device = _gson.fromJson(deviceJson.getAsJsonObject(), User.class);

            return Optional.of(device);
        }));
    }

    public HTTPApiRequest<Object> unpairDevice(int deviceId) {
        Request request = ApiUtils.getAuthorizedRequestBuilder().url(_devicesApiBase + "/device/" + deviceId + "/unpair/").build();

        return ApiUtils.createApiRequest(request, ((response, applicationResponse) -> new Object()));
    }
}
