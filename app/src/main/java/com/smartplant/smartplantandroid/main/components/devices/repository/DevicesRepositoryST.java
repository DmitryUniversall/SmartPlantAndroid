package com.smartplant.smartplantandroid.main.components.devices.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.JsonObject;
import com.smartplant.smartplantandroid.core.logs.AppLogger;
import com.smartplant.smartplantandroid.core.network.http.http_api_request.HTTPApiRequest;
import com.smartplant.smartplantandroid.main.components.auth.models.User;
import com.smartplant.smartplantandroid.main.components.devices.internal_utils.DevicesApiService;
import com.smartplant.smartplantandroid.main.components.devices.internal_utils.DevicesStorageService;
import com.smartplant.smartplantandroid.main.components.sensors_data.models.SensorsData;
import com.smartplant.smartplantandroid.main.components.sensors_data.repository.SensorsDataRepositoryST;
import com.smartplant.smartplantandroid.main.components.storage.utils.storage_request.StorageRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DevicesRepositoryST {
    // Singleton
    private static @Nullable DevicesRepositoryST _instance;

    // Utils
    private final @NonNull DevicesApiService _apiService;
    private final @NonNull DevicesStorageService _storageService;
    private final SensorsDataRepositoryST _sensorsDataRepository;

    // Cache
    private boolean _isLoaded = false;
    private @NonNull Map<Integer, User> _myDevices = new HashMap<>();

    public static synchronized void createInstance() {
        if (_instance != null)
            throw new RuntimeException("DevicesRepositoryST has already been initialized");
        _instance = new DevicesRepositoryST();
    }

    public static synchronized DevicesRepositoryST getInstance() {
        if (_instance == null)
            throw new RuntimeException("DevicesRepositoryST has not been initialized");
        return _instance;
    }

    private DevicesRepositoryST() {
        this._apiService = new DevicesApiService();
        this._storageService = new DevicesStorageService();
        this._sensorsDataRepository = SensorsDataRepositoryST.getInstance();
    }

    public boolean isLoaded() {
        return this._isLoaded;
    }

    public void addDevice(@NonNull User device) {
        this._myDevices.put(device.getId(), device);
    }

    public Map<Integer, User> getMyDevices() {
        return this._myDevices;
    }

    public HTTPApiRequest<Map<Integer, User>> fetchMyDevices() {
        this._isLoaded = true;
        return this._apiService.fetchMyDevices().onSuccess((result, response, applicationResponse) -> this._myDevices = result);
    }

    public HTTPApiRequest<Optional<User>> pairDevice(String deviceUsername) {
        return this._apiService.pairDevice(deviceUsername).onSuccess((result, response, applicationResponse) -> {
            if (!result.isPresent()) return;
            User deviceUser = result.get();
            this._myDevices.put(deviceUser.getId(), deviceUser);
        });
    }

    public HTTPApiRequest<Void> unpairDevice(int deviceId) {
        return this._apiService.unpairDevice(deviceId).onSuccess((result, response, applicationResponse) -> this._myDevices.remove(deviceId));
    }

    public StorageRequest<SensorsData> requestSensorsData(int deviceId, int timeout, @Nullable JsonObject data) {
        return this._storageService.requestSensorsData(deviceId, timeout, data).onSuccess(
                (result, dataMessage, response) -> _sensorsDataRepository.insertSensorsData(deviceId, result).execute()  // Save latest data to db
        );
    }

    public StorageRequest<Void> requestSensorsDataUpdate(int deviceId, int dataUpdateInterval, int timeout) {
        return this._storageService.requestSensorsDataUpdate(deviceId, dataUpdateInterval, timeout).onSuccess(
                (result, dataMessage, response) -> AppLogger.info("Sensors data update request success; Code: %d", response.getApplicationStatusCode())
        );
    }

    public StorageRequest<Boolean> commandLamp(int deviceId, int timeout, boolean newState, boolean toggle) {
        return this._storageService.commandLamp(deviceId, timeout, newState, toggle);
    }

    public StorageRequest<Void> commandIrrigate(int deviceId, int timeout) {
        return this._storageService.commandIrrigate(deviceId, timeout);
    }

    @Nullable
    public User getDeviceById(int deviceId) {
        return this._myDevices.get(deviceId);
    }
}
