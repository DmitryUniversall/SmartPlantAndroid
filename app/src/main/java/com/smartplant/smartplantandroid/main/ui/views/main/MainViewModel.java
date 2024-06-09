package com.smartplant.smartplantandroid.main.ui.views.main;

import androidx.lifecycle.ViewModel;

import com.smartplant.smartplantandroid.core.logs.AppLogger;
import com.smartplant.smartplantandroid.main.components.storage.repository.StorageRepositoryST;

public class MainViewModel extends ViewModel {
    private final StorageRepositoryST storageRepository;

    public MainViewModel() {
        this.storageRepository = StorageRepositoryST.getInstance();
    }

    public void connectStorageWS() {
        storageRepository.connect().onDAR("MainViewModel", (dataMessage, response) -> AppLogger.info("Got device application response (%s; code %d)", response.isOk() ? "OK" : "ERROR", response.getApplicationStatusCode()));
    }
}
