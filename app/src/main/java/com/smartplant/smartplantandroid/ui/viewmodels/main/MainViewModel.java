package com.smartplant.smartplantandroid.ui.viewmodels.main;

import androidx.lifecycle.ViewModel;

import com.smartplant.smartplantandroid.storage.repository.StorageRepositoryST;
import com.smartplant.smartplantandroid.utils.AppLogger;

public class MainViewModel extends ViewModel {
    private final StorageRepositoryST storageRepository;

    public MainViewModel() {
        this.storageRepository = StorageRepositoryST.getInstance();
    }

    public void connectStorageWS() {
        storageRepository.connect()
                .onServerApplicationResponse(response -> AppLogger.info("Got server application response"))
                .onDeviceApplicationResponse((dataMessage, response) -> AppLogger.info("Got device application response"));
    }
}
