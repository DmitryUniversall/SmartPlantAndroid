package com.smartplant.smartplantandroid.main.ui.views.main.navigation.devices.device_detail;

import androidx.annotation.Nullable;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.smartplant.smartplantandroid.core.async.background_task.BackgroundTask;
import com.smartplant.smartplantandroid.core.logs.AppLogger;
import com.smartplant.smartplantandroid.main.components.auth.models.User;
import com.smartplant.smartplantandroid.main.components.devices.repository.DevicesRepositoryST;
import com.smartplant.smartplantandroid.main.components.sensors_data.models.SensorsData;
import com.smartplant.smartplantandroid.main.components.sensors_data.repository.SensorsDataRepositoryST;
import com.smartplant.smartplantandroid.main.components.storage.utils.StorageRequestResult;
import com.smartplant.smartplantandroid.main.components.storage.utils.storage_request.StorageRequest;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DeviceDetailViewModel extends ViewModel {
    private final DevicesRepositoryST _devicesRepository;
    private final SensorsDataRepositoryST _sensorsDataRepository;

    public DeviceDetailViewModel() {
        this._devicesRepository = DevicesRepositoryST.getInstance();
        this._sensorsDataRepository = SensorsDataRepositoryST.getInstance();
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

    public StorageRequest<Void> requestSensorsDataUpdate(int deviceId, int dataUpdateInterval, int timeout) {
        return this._devicesRepository.requestSensorsDataUpdate(deviceId, dataUpdateInterval, timeout)
                .onSuccess((result, dataMessage, response) -> AppLogger.info("Data update request for device id=%d responded with success", deviceId))
                .onFailure(error -> AppLogger.info("Data update request failed for device id=%d", deviceId));
    }

    public BackgroundTask<List<SensorsData>> getDailySensorsData(int device_id) {
        Calendar calendarMidnight = Calendar.getInstance();
        calendarMidnight.set(calendarMidnight.get(Calendar.YEAR), calendarMidnight.get(Calendar.MONTH), calendarMidnight.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        return this._sensorsDataRepository.getSensorsDataByDate(device_id, calendarMidnight.getTime(), new Date());
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
}
