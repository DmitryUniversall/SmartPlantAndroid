package com.smartplant.smartplantandroid.main.components.devices.internal_utils;

import static com.smartplant.smartplantandroid.core.data.json.JsonUtils.getGson;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.smartplant.smartplantandroid.core.data.json.JsonUtils;
import com.smartplant.smartplantandroid.core.exceptions.HasNoDataException;
import com.smartplant.smartplantandroid.main.components.sensors_data.models.SensorsData;
import com.smartplant.smartplantandroid.main.components.storage.internal_utils.processors.actions.StorageWSActionProcessor;
import com.smartplant.smartplantandroid.main.components.storage.utils.storage_request.StorageRequest;
import com.smartplant.smartplantandroid.main.components.storage.models.StorageAction;
import com.smartplant.smartplantandroid.main.components.storage.repository.StorageRepositoryST;

public class DevicesStorageService {
    // Utils
    private static final Gson _gson = getGson();
    private final @NonNull StorageRepositoryST _storageRepository;

    public DevicesStorageService() {
        this._storageRepository = StorageRepositoryST.getInstance();
    }

    public StorageRequest<SensorsData> requestSensorsData(int deviceId, int timeout, @Nullable JsonObject data) {
        StorageWSActionProcessor processor = this._storageRepository.getProcessor();
        StorageAction action = new StorageAction(StorageAction.ApplicationActionType.REQUEST_SENSORS_DATA.getValue(), data);

        return processor.<SensorsData>getStorageRequestBuilder()
                .setTargetId(deviceId)
                .setPayloadData(_gson.toJsonTree(action).getAsJsonObject())
                .setTimeout(timeout)
                .setResponseProcessor((dataMessage, applicationResponse) -> {
                    if (applicationResponse.getData() == null) throw new HasNoDataException();
                    SensorsData sensorsData = JsonUtils.fromJsonWithNulls(applicationResponse.getData(), SensorsData.class);
                    sensorsData.setDeviceId(deviceId);
                    sensorsData.setCreatedAt(dataMessage.getCreatedAt());  // TODO: Refactor it
                    return sensorsData;
                })
                .build();
    }

    public StorageRequest<Void> requestSensorsDataUpdate(int deviceId, int dataUpdateInterval, int timeout) {
        StorageWSActionProcessor processor = this._storageRepository.getProcessor();

        JsonObject data = new JsonObject();
        data.addProperty("interval", dataUpdateInterval);
        StorageAction action = new StorageAction(StorageAction.ApplicationActionType.REQUEST_SENSORS_DATA_UPDATE.getValue(), data);

        return processor.<Void>getStorageRequestBuilder()
                .setTargetId(deviceId)
                .setTimeout(timeout)
                .setPayloadData(_gson.toJsonTree(action).getAsJsonObject())
                .setResponseProcessor((dataMessage, applicationResponse) -> null)
                .build();
    }

    public StorageRequest<Boolean> commandLamp(int deviceId, int timeout, boolean newState, boolean toggle) {
        StorageWSActionProcessor processor = this._storageRepository.getProcessor();

        JsonObject data = new JsonObject();
        data.addProperty("set_state", newState);
        data.addProperty("toggle", toggle);
        StorageAction action = new StorageAction(StorageAction.ApplicationActionType.COMMAND_LAMP.getValue(), data);

        return processor.<Boolean>getStorageRequestBuilder()
                .setTargetId(deviceId)
                .setTimeout(timeout)
                .setPayloadData(_gson.toJsonTree(action).getAsJsonObject())
                .setResponseProcessor(((dataMessage, applicationResponse) -> {
                    assert applicationResponse.getData() != null;
                    JsonElement state = applicationResponse.getData().get("state");
                    return state != null && state.getAsBoolean();
                }))
                .build();
    }

    public StorageRequest<Void> commandIrrigate(int deviceId, int timeout) {
        StorageWSActionProcessor processor = this._storageRepository.getProcessor();

        StorageAction action = new StorageAction(StorageAction.ApplicationActionType.COMMAND_IRRIGATE.getValue(), null);

        return processor.<Void>getStorageRequestBuilder()
                .setTargetId(deviceId)
                .setTimeout(timeout)
                .setPayloadData(_gson.toJsonTree(action).getAsJsonObject())
                .setResponseProcessor(((dataMessage, applicationResponse) -> null))
                .build();
    }
}
