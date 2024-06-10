package com.smartplant.smartplantandroid.main.components.storage.utils.storage_request;

import androidx.annotation.NonNull;

import com.smartplant.smartplantandroid.core.models.ApplicationResponse;
import com.smartplant.smartplantandroid.main.components.storage.models.StorageDataMessage;

public interface StorageRequestSuccessCallback<T> {
    void onSuccess(T result, @NonNull StorageDataMessage dataMessage, @NonNull ApplicationResponse response);
}
