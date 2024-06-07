package com.smartplant.smartplantandroid.main.components.devices.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.smartplant.smartplantandroid.core.network.http.http_api_request.HTTPApiRequest;
import com.smartplant.smartplantandroid.main.components.auth.models.User;
import com.smartplant.smartplantandroid.main.components.devices.internal_utils.DevicesApiService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DevicesRepositoryST {
    // Singleton
    private static @Nullable DevicesRepositoryST _instance;

    // Utils
    private final @NonNull DevicesApiService _service;

    // Cache
    private boolean _isLoaded = false;
    private final @NonNull Map<Integer, User> _myDevices = new HashMap<>();

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
        this._service = new DevicesApiService();
    }

    public boolean isLoaded() {
        return this._isLoaded;
    }

    public Map<Integer, User> getMyDevices() {
        return this._myDevices;
    }

    public HTTPApiRequest<Map<Integer, User>> fetchMyDevices() {
        this._isLoaded = true;
        return this._service.fetchMyDevices().onSuccess((result, response, applicationResponse) -> this._myDevices.putAll(result));
    }

    public HTTPApiRequest<Optional<User>> pairDevice(String deviceUsername) {
        return this._service.pairDevice(deviceUsername).onSuccess((result, response, applicationResponse) -> {
            if (!result.isPresent()) return;
            User deviceUser = result.get();
            this._myDevices.put(deviceUser.getId(), deviceUser);
        });
    }

    public HTTPApiRequest<Object> unpairDevice(int deviceId) {
        return this._service.unpairDevice(deviceId).onSuccess((result, response, applicationResponse) -> this._myDevices.remove(deviceId));
    }
}
