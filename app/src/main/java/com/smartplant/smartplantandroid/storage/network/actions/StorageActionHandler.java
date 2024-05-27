package com.smartplant.smartplantandroid.storage.network.actions;

import androidx.annotation.NonNull;

import com.smartplant.smartplantandroid.storage.models.StorageAction;
import com.smartplant.smartplantandroid.storage.models.StorageDataMessage;

public interface StorageActionHandler {
    void onAction(@NonNull StorageDataMessage dataMessage, @NonNull StorageAction storageAction);
}
