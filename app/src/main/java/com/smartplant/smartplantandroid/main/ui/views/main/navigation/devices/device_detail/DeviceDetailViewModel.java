package com.smartplant.smartplantandroid.main.ui.views.main.navigation.devices.device_detail;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.smartplant.smartplantandroid.main.components.devices.repository.DevicesRepositoryST;
import com.smartplant.smartplantandroid.main.components.sensors_data.internal_utils.db.SensorsDataDBManagerST;
import com.smartplant.smartplantandroid.main.components.sensors_data.models.SensorsData;
import com.smartplant.smartplantandroid.main.components.storage.utils.StorageRequestResult;

public class DeviceDetailViewModel extends ViewModel {
    private final DevicesRepositoryST _devicesRepository;
    private final SensorsDataDBManagerST _sensorsDataDBManager;

    private final MutableLiveData<SensorsData> _sensorsData = new MutableLiveData<>();

    public DeviceDetailViewModel() {
        this._devicesRepository = DevicesRepositoryST.getInstance();
        this._sensorsDataDBManager = SensorsDataDBManagerST.getInstance();
    }

    public MutableLiveData<StorageRequestResult<SensorsData>> requestSensorsData(int deviceId, int timeout) {
        MutableLiveData<StorageRequestResult<SensorsData>> requestResult = new MediatorLiveData<>();

        this._devicesRepository.requestSensorsData(deviceId, timeout)
                .onSuccess((result, response, dataMessage) -> {
                    _sensorsDataDBManager.insertSensorsData(result);  // Save latest data to db
                    requestResult.postValue(new StorageRequestResult<>(true, result, response, dataMessage, null));
                })
                .onFailure(error -> requestResult.postValue(new StorageRequestResult<>(false, null, null, null, error)))
                .send();

        return requestResult;
    }

//    public MutableLiveData<StorageRequestResult<Object>> setLampState(boolean newState, int timeout) {
//        MutableLiveData<StorageRequestResult<Object>> requestResult = new MediatorLiveData<>();
//
//        StorageWSActionProcessor processor = getProcessor();
//        JsonObject data = new JsonObject();
//        data.addProperty("mode", newState ? 1 : 0);
//        StorageAction action = new StorageAction(StorageAction.ApplicationActionType.COMMAND_LAMP.getValue(), data);
//
//        StorageRequest<Object> request = processor.getStorageRequestBuilder()
//                .setTargetId(2)
//                .setPayloadData(_gson.toJsonTree(action).getAsJsonObject())
//                .setTimeout(timeout)
//                .setResponseProcessor((applicationResponse, dataMessage) -> new Object())
//                .build();
//
//        request.onSuccess((result, response, dataMessage) -> requestResult.postValue(new StorageRequestResult<>(true, result, response, dataMessage, null)))
//                .onFailure(error -> requestResult.postValue(new StorageRequestResult<>(false, null, null, null, error)))
//                .send();
//
//        return requestResult;
//    }
}
