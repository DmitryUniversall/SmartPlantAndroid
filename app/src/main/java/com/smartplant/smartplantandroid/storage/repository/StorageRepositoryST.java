package com.smartplant.smartplantandroid.storage.repository;

import androidx.annotation.Nullable;

import com.smartplant.smartplantandroid.storage.network.StorageWSService;
import com.smartplant.smartplantandroid.storage.network.actions.ActionProcessor;

public class StorageRepositoryST {
    private static @Nullable StorageRepositoryST _instance;
    private final StorageWSService _service;

    public static synchronized void createInstance() {
        if (_instance != null)
            throw new RuntimeException("AuthRepositoryST has already been initialized");
        _instance = new StorageRepositoryST();
    }

    public static synchronized StorageRepositoryST getInstance() {
        if (_instance == null)
            throw new RuntimeException("AuthRepositoryST has not been initialized");
        return _instance;
    }

    private StorageRepositoryST() {
        this._service = new StorageWSService();
    }

    public ActionProcessor connect() {
        return _service.connect();
    }

    @Nullable
    public ActionProcessor getProcessor() {
        return this._service.getProcessor();
    }
}
