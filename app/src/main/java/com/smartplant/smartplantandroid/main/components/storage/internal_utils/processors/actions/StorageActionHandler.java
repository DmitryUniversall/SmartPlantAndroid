package com.smartplant.smartplantandroid.main.components.storage.internal_utils.processors.actions;

import androidx.annotation.NonNull;

import com.smartplant.smartplantandroid.main.components.storage.models.StorageAction;
import com.smartplant.smartplantandroid.main.components.storage.models.StorageDataMessage;

public interface StorageActionHandler {
    void onAction(@NonNull StorageDataMessage dataMessage, @NonNull StorageAction storageAction);
}
