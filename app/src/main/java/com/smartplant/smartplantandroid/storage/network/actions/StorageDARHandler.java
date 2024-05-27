package com.smartplant.smartplantandroid.storage.network.actions;

import androidx.annotation.NonNull;

import com.smartplant.smartplantandroid.storage.models.StorageDataMessage;
import com.smartplant.smartplantandroid.utils.network.ApplicationResponse;

// Storage Device Application Response Handler
public interface StorageDARHandler {
    void onDeviceApplicationResponse(@NonNull StorageDataMessage dataMessage, @NonNull ApplicationResponse response);
}
