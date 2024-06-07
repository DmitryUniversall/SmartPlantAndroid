package com.smartplant.smartplantandroid.main.components.storage.repository;

import androidx.annotation.Nullable;

import com.smartplant.smartplantandroid.main.components.storage.internal_utils.StorageWSService;
import com.smartplant.smartplantandroid.main.components.storage.internal_utils.processors.actions.StorageWSActionProcessor;

public class StorageRepositoryST {
    private static @Nullable StorageRepositoryST _instance;
    private final StorageWSService _service;

    public static synchronized void createInstance() {
        if (_instance != null) throw new RuntimeException("StorageRepositoryST has already been initialized");
        _instance = new StorageRepositoryST();
    }

    public static synchronized StorageRepositoryST getInstance() {
        if (_instance == null) throw new RuntimeException("StorageRepositoryST has not been initialized");
        return _instance;
    }

    private StorageRepositoryST() {
        this._service = new StorageWSService();
    }

    public StorageWSActionProcessor connect() {
        return _service.connect();
    }

    @Nullable
    public StorageWSActionProcessor getProcessor() {
        return this._service.getProcessor();
    }
}
