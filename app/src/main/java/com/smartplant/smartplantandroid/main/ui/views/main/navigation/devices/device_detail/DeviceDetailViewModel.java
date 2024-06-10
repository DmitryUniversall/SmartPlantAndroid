package com.smartplant.smartplantandroid.main.ui.views.main.navigation.devices.device_detail;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.smartplant.smartplantandroid.main.components.devices.repository.DevicesRepositoryST;
import com.smartplant.smartplantandroid.main.components.sensors_data.models.SensorsData;
import com.smartplant.smartplantandroid.main.components.storage.utils.StorageRequestResult;

public class DeviceDetailViewModel extends ViewModel {
    private final DevicesRepositoryST _devicesRepository;

    private final MutableLiveData<SensorsData> _sensorsLiveData = new MutableLiveData<>();

    public DeviceDetailViewModel() {
        this._devicesRepository = DevicesRepositoryST.getInstance();
    }

    public MutableLiveData<StorageRequestResult<SensorsData>> requestSensorsData(int deviceId, int timeout) {
        MutableLiveData<StorageRequestResult<SensorsData>> requestResult = new MediatorLiveData<>();

        this._devicesRepository.requestSensorsData(deviceId, timeout)  // TODO: Update _sensorsLiveData
                .onSuccess((result, dataMessage, response) -> requestResult.postValue(new StorageRequestResult<>(true, result, response, dataMessage, null)))
                .onFailure(error -> requestResult.postValue(new StorageRequestResult<>(false, null, null, null, error)))
                .send();

        return requestResult;
    }
}
