package com.smartplant.smartplantandroid.main.components.storage.internal_utils;

import static com.smartplant.smartplantandroid.core.data.json.JsonUtils.getGson;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.smartplant.smartplantandroid.core.network.ApiUtils;
import com.smartplant.smartplantandroid.core.network.http.http_api_request.HTTPApiRequest;
import com.smartplant.smartplantandroid.main.components.storage.models.write.StorageRequestPayload;
import com.smartplant.smartplantandroid.main.components.storage.models.write.StorageResponsePayload;

import okhttp3.Request;
import okhttp3.RequestBody;

public class StorageApiService {
    // Utils
    private static final Gson _gson = getGson();

    protected final String _storageApiBase;

    public StorageApiService() {
        this._storageApiBase = ApiUtils.getBaseURL("http") + "storage/";
    }

    public HTTPApiRequest<String> writeRequest(@NonNull StorageRequestPayload payload) {
        RequestBody body = ApiUtils.createJsonRequestBody(_gson.toJson(payload));
        Request request = ApiUtils.getAuthorizedRequestBuilder().url(_storageApiBase + "write/request/").post(body).build();

        return ApiUtils.createApiRequest(request, ((response, applicationResponse) -> {
            JsonObject data = applicationResponse.getData();
            assert data != null;
            return data.get("request_uuid").getAsString();
        }));
    }

    public HTTPApiRequest<Void> writeResponse(@NonNull StorageResponsePayload payload) {
        RequestBody body = ApiUtils.createJsonRequestBody(_gson.toJson(payload));
        Request request = ApiUtils.getAuthorizedRequestBuilder().url(_storageApiBase + "write/response/").post(body).build();

        return ApiUtils.createApiRequest(request, (response, applicationResponse) -> null);
    }
}
