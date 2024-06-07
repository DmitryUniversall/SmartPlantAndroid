package com.smartplant.smartplantandroid.main.components.storage.internal_utils.processors;

import androidx.annotation.NonNull;

import com.smartplant.smartplantandroid.core.models.ApplicationResponse;
import com.smartplant.smartplantandroid.main.components.storage.models.StorageDataMessage;

public interface StorageDARHandler {
    void onDeviceApplicationResponse(@NonNull StorageDataMessage dataMessage, @NonNull ApplicationResponse response);
}
