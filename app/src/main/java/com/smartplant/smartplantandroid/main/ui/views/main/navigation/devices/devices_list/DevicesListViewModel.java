package com.smartplant.smartplantandroid.main.ui.views.main.navigation.devices.devices_list;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.smartplant.smartplantandroid.core.network.http.http_api_request.HTTPApiRequest;
import com.smartplant.smartplantandroid.main.components.auth.models.User;
import com.smartplant.smartplantandroid.main.components.devices.repository.DevicesRepositoryST;

import java.util.Map;
import java.util.Optional;

public class DevicesListViewModel extends ViewModel {
    private final @NonNull DevicesRepositoryST _devicesRepository;
    private final @NonNull MutableLiveData<Map<Integer, User>> _devicesLiveData = new MutableLiveData<>();

    public DevicesListViewModel() {
        this._devicesRepository = DevicesRepositoryST.getInstance();
        this._devicesLiveData.setValue(_devicesRepository.getMyDevices());
    }

    public boolean isLoaded() {
        return this._devicesRepository.isLoaded();
    }

    public Map<Integer, User> getDevices() {
        return this._devicesRepository.getMyDevices();
    }

    public MutableLiveData<Map<Integer, User>> getDevicesLiveData() {
        return _devicesLiveData;
    }

    public HTTPApiRequest<Map<Integer, User>> fetchMyDevices() {
        return this._devicesRepository.fetchMyDevices().onSuccess((result, response, applicationResponse) -> this._devicesLiveData.postValue(this._devicesRepository.getMyDevices()));
    }

    public HTTPApiRequest<Optional<User>> pairDevice(String deviceUsername) {
        return this._devicesRepository.pairDevice(deviceUsername).onSuccess((result, response, applicationResponse) -> this._devicesLiveData.postValue(this._devicesRepository.getMyDevices()));
    }

    public HTTPApiRequest<Object> unpairDevice(int deviceId) {
        return this._devicesRepository.unpairDevice(deviceId).onSuccess((result, response, applicationResponse) -> this._devicesLiveData.postValue(this._devicesRepository.getMyDevices()));
    }
}
