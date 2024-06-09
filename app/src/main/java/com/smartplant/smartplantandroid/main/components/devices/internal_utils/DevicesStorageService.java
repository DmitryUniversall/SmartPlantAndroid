package com.smartplant.smartplantandroid.main.components.devices.internal_utils;

import static com.smartplant.smartplantandroid.core.data.json.JsonUtils.getGson;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.smartplant.smartplantandroid.core.data.json.JsonUtils;
import com.smartplant.smartplantandroid.core.exceptions.HasNoDataException;
import com.smartplant.smartplantandroid.main.components.sensors_data.models.SensorsData;
import com.smartplant.smartplantandroid.main.components.storage.internal_utils.processors.actions.StorageWSActionProcessor;
import com.smartplant.smartplantandroid.main.components.storage.internal_utils.storage_request.StorageRequest;
import com.smartplant.smartplantandroid.main.components.storage.models.StorageAction;
import com.smartplant.smartplantandroid.main.components.storage.repository.StorageRepositoryST;

public class DevicesStorageService {
    // Utils
    private static final Gson _gson = getGson();
    private final @NonNull StorageRepositoryST _storageRepository;

    public DevicesStorageService() {
        this._storageRepository = StorageRepositoryST.getInstance();
    }

    public StorageRequest<SensorsData> requestSensorsData(int deviceId, int timeout) {
        StorageWSActionProcessor processor = this._storageRepository.getProcessor();
        StorageAction action = new StorageAction(StorageAction.ApplicationActionType.REQUEST_SENSORS_DATA.getValue(), null);

        return processor.<SensorsData>getStorageRequestBuilder()
                .setTargetId(deviceId)
                .setPayloadData(_gson.toJsonTree(action).getAsJsonObject())
                .setTimeout(timeout)
                .setResponseProcessor((dataMessage, applicationResponse) -> {
                    if (applicationResponse.getData() == null) throw new HasNoDataException();
                    return JsonUtils.fromJsonWithNulls(applicationResponse.getData(), SensorsData.class);
                })
                .build();
    }
}
