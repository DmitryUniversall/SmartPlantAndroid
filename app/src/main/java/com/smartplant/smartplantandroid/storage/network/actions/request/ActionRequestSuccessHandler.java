package com.smartplant.smartplantandroid.storage.network.actions.request;

import androidx.annotation.NonNull;

import com.smartplant.smartplantandroid.storage.models.StorageDataMessage;
import com.smartplant.smartplantandroid.utils.network.ApplicationResponse;

public interface ActionRequestSuccessHandler {
    void onSuccess(@NonNull ApplicationResponse response, @NonNull StorageDataMessage dataMessage);
}
