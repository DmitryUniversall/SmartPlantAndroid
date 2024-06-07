package com.smartplant.smartplantandroid.main.ui.views.main.navigation.devices.device_detail;

import static com.smartplant.smartplantandroid.core.data.json.JsonUtils.getGson;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.smartplant.smartplantandroid.main.components.storage.internal_utils.processors.actions.StorageWSActionProcessor;
import com.smartplant.smartplantandroid.main.components.storage.internal_utils.storage_request.StorageRequest;
import com.smartplant.smartplantandroid.main.components.storage.models.StorageAction;
import com.smartplant.smartplantandroid.main.components.sensors_data.models.SensorsData;
import com.smartplant.smartplantandroid.main.components.storage.repository.StorageRepositoryST;
import com.smartplant.smartplantandroid.main.components.storage.utils.StorageRequestResult;

public class DeviceDetailViewModel extends ViewModel {
    private static final Gson _gson = getGson();

    private final StorageRepositoryST storageRepository;
    private final MutableLiveData<SensorsData> _sensorsData = new MutableLiveData<>();

    public DeviceDetailViewModel() {
        this.storageRepository = StorageRepositoryST.getInstance();
    }

    public StorageWSActionProcessor getProcessor() {
        StorageWSActionProcessor actionProcessor = storageRepository.getProcessor();
        if (actionProcessor == null)
            throw new IllegalStateException("Unable to set lamp state: not connected");

        return actionProcessor;
    }

    public MutableLiveData<StorageRequestResult<Object>> setLampState(boolean newState, int timeout) {
        MutableLiveData<StorageRequestResult<Object>> requestResult = new MediatorLiveData<>();

        StorageWSActionProcessor processor = getProcessor();
        JsonObject data = new JsonObject();
        data.addProperty("mode", newState ? 1 : 0);
        StorageAction action = new StorageAction(StorageAction.ApplicationActionType.COMMAND_LAMP.getValue(), data);

        StorageRequest<Object> request = processor.getStorageRequestBuilder()
                .setTargetId(2)
                .setPayloadData(_gson.toJsonTree(action).getAsJsonObject())
                .setTimeout(timeout)
                .setResponseProcessor((applicationResponse, dataMessage) -> new Object())
                .build();

        request.onSuccess((result, response, dataMessage) -> requestResult.postValue(new StorageRequestResult<>(true, result, response, dataMessage, null)))
                .onFailure(error -> requestResult.postValue(new StorageRequestResult<>(false, null, null, null, error)))
                .send();

        return requestResult;
    }
}
