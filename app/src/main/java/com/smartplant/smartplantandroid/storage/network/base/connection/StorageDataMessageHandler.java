package com.smartplant.smartplantandroid.storage.network.base.connection;

import androidx.annotation.NonNull;

import com.smartplant.smartplantandroid.storage.models.StorageDataMessage;

public interface StorageDataMessageHandler {
    void onDataMessage(@NonNull StorageDataMessage dataMessage);
}
