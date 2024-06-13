package com.smartplant.smartplantandroid.main.ui.views.main.navigation.devices.device_detail;

import androidx.annotation.Nullable;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.smartplant.smartplantandroid.core.logs.AppLogger;
import com.smartplant.smartplantandroid.main.components.auth.models.User;
import com.smartplant.smartplantandroid.main.components.devices.repository.DevicesRepositoryST;
import com.smartplant.smartplantandroid.main.components.sensors_data.models.SensorsData;
import com.smartplant.smartplantandroid.main.components.sensors_data.repository.SensorsDataRepositoryST;
import com.smartplant.smartplantandroid.main.components.storage.utils.StorageRequestResult;
import com.smartplant.smartplantandroid.main.components.storage.utils.storage_request.StorageRequest;

public class DeviceDetailViewModel extends ViewModel {
    private final DevicesRepositoryST _devicesRepository;
    private final SensorsDataRepositoryST _sensorsDataRepository;

    private final MutableLiveData<SensorsData> _sensorsLiveData = new MutableLiveData<>();

    public DeviceDetailViewModel() {
        this._devicesRepository = DevicesRepositoryST.getInstance();
        this._sensorsDataRepository = SensorsDataRepositoryST.getInstance();
    }

    public MutableLiveData<SensorsData> getSensorsLiveData() {
        return _sensorsLiveData;
    }

    @Nullable
    public User getDeviceById(int deviceId) {
        return this._devicesRepository.getDeviceById(deviceId);
    }

    public StorageRequest<SensorsData> requestSensorsData(int deviceId, int timeout) {
        return this._devicesRepository.requestSensorsData(deviceId, timeout)
                .onSuccess((result, dataMessage, response) -> AppLogger.info("Successfully fetched sensors data"))
                .onFailure(error -> AppLogger.info("Failed to fetch sensors data"));
    }

    public StorageRequest<Void> requestSensorsDataUpdate(int deviceId, int timeout) {
        return this._devicesRepository.requestSensorsDataUpdate(deviceId, timeout)
                .onSuccess((result, dataMessage, response) -> AppLogger.info("Data update request for device id=%d responded with success", deviceId))
                .onFailure(error -> AppLogger.info("Data update request failed for device id=%d", deviceId));
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
