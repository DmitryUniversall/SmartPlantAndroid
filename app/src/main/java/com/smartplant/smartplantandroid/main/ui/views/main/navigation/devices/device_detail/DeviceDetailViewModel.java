package com.smartplant.smartplantandroid.main.ui.views.main.navigation.devices.device_detail;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;
import com.smartplant.smartplantandroid.core.async.background_task.BackgroundTask;
import com.smartplant.smartplantandroid.core.data.json.JsonUtils;
import com.smartplant.smartplantandroid.core.logs.AppLogger;
import com.smartplant.smartplantandroid.main.components.auth.models.User;
import com.smartplant.smartplantandroid.main.components.devices.repository.DevicesRepositoryST;
import com.smartplant.smartplantandroid.main.components.sensors_data.models.SensorsData;
import com.smartplant.smartplantandroid.main.components.sensors_data.repository.SensorsDataRepositoryST;
import com.smartplant.smartplantandroid.main.components.storage.models.StorageAction;
import com.smartplant.smartplantandroid.main.components.storage.models.StorageDataMessage;
import com.smartplant.smartplantandroid.main.components.storage.repository.StorageRepositoryST;
import com.smartplant.smartplantandroid.main.components.storage.utils.StorageRequestResult;
import com.smartplant.smartplantandroid.main.components.storage.utils.storage_request.StorageRequest;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class DeviceDetailViewModel extends ViewModel {
    private final DevicesRepositoryST _devicesRepository;
    private final SensorsDataRepositoryST _sensorsDataRepository;
    private final StorageRepositoryST _storageRepositoryST;

    private final MutableLiveData<SensorsData> _sensorsLiveData = new MutableLiveData<>();
    private final String processorActionName = "DeviceDetailViewModel_" + UUID.randomUUID().toString();

    public DeviceDetailViewModel() {
        this._devicesRepository = DevicesRepositoryST.getInstance();
        this._sensorsDataRepository = SensorsDataRepositoryST.getInstance();
        this._storageRepositoryST = StorageRepositoryST.getInstance();
        _storageRepositoryST.getProcessor()
                .onAction(
                        StorageAction.DeviceActionType.SENSORS_DATA_UPDATE.getValue(),
                        processorActionName,  // TODO: Hack
                        this::onNewSensorsData
                );
    }

    @Override
    protected void onCleared() {
        _storageRepositoryST.getProcessor()
                .removeActionHandler(
                        StorageAction.DeviceActionType.SENSORS_DATA_UPDATE.getValue(),
                        processorActionName
                );
    }

    private void onNewSensorsData(@NonNull StorageDataMessage dataMessage, @NonNull StorageAction storageAction) {
        JsonObject data = storageAction.getData();
        assert data != null;
        SensorsData sensorsData = JsonUtils.fromJsonWithNulls(data, SensorsData.class);
        sensorsData.setCreatedAt(dataMessage.getCreatedAt());  // TODO: Refactor it

        this._sensorsLiveData.postValue(sensorsData);
    }

    public MutableLiveData<SensorsData> getSensorsLiveData() {
        return _sensorsLiveData;
    }

    @Nullable
    public User getDeviceById(int deviceId) {
        return this._devicesRepository.getDeviceById(deviceId);
    }

    public StorageRequest<SensorsData> requestSensorsData(int deviceId, int timeout) {
        return this._devicesRepository.requestSensorsData(deviceId, timeout, null)
                .onSuccess((result, dataMessage, response) -> AppLogger.info("Successfully fetched sensors data"))
                .onFailure(error -> AppLogger.info("Failed to fetch sensors data"));
    }

    public StorageRequest<Void> requestSensorsDataUpdate(int deviceId, int timeout) {
        return this._devicesRepository.requestSensorsDataUpdate(deviceId, timeout)
                .onSuccess((result, dataMessage, response) -> AppLogger.info("Data update request for device id=%d responded with success", deviceId))
                .onFailure(error -> AppLogger.info("Data update request failed for device id=%d", deviceId));
    }

    public BackgroundTask<List<SensorsData>> getDailySensorsData(int device_id) {
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.HOUR_OF_DAY, -24);
        Date yesterday = calendar.getTime();
        return this._sensorsDataRepository.getSensorsDataByDate(device_id, yesterday, now);
    }

    public StorageRequest<Boolean> commandLamp(int deviceId, int timeout, boolean newState, boolean toggle) {
        return this._devicesRepository.commandLamp(deviceId, timeout, newState, toggle);
    }

    public StorageRequest<Void> commandIrrigate(int deviceId, int timeout) {
        return this._devicesRepository.commandIrrigate(deviceId, timeout);
    }

    public MutableLiveData<StorageRequestResult<SensorsData>> liveRequestSensorsData(int deviceId, int timeout) {
        MutableLiveData<StorageRequestResult<SensorsData>> requestResult = new MediatorLiveData<>();

        this.requestSensorsData(deviceId, timeout)
                .onSuccess((result, dataMessage, response) -> requestResult.postValue(new StorageRequestResult<>(true, result, response, dataMessage, null)))
                .onFailure(error -> requestResult.postValue(new StorageRequestResult<>(false, null, null, null, error)))
                .send();

        return requestResult;
    }

    public SensorsData getCachedSensorsData(int deviceId) {
        return this._sensorsDataRepository.getCachedSensorsData(deviceId);
    }

    public void updateSensorsData(int deviceId, int timeout) {
        SensorsData sensorsData = this.getCachedSensorsData(deviceId);
        if (sensorsData != null) {  // Get form cache if exist, send request otherwise
            this._sensorsLiveData.postValue(sensorsData);
        } else {
            this.requestSensorsData(deviceId, timeout)
                    .onSuccess(((result, dataMessage, response) -> this._sensorsLiveData.postValue(result)))
                    .send();
        }
    }
}
