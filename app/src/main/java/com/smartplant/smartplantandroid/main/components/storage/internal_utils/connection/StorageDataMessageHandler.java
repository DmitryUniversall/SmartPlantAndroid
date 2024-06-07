package com.smartplant.smartplantandroid.main.components.storage.internal_utils.connection;

import androidx.annotation.NonNull;

import com.smartplant.smartplantandroid.main.components.storage.models.StorageDataMessage;

public interface StorageDataMessageHandler {
    void onDataMessage(@NonNull StorageDataMessage dataMessage);
}
