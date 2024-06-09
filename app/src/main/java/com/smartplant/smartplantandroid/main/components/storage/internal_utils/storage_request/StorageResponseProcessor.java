package com.smartplant.smartplantandroid.main.components.storage.internal_utils.storage_request;

import androidx.annotation.NonNull;

import com.smartplant.smartplantandroid.core.models.ApplicationResponse;
import com.smartplant.smartplantandroid.main.components.storage.models.StorageDataMessage;

public interface StorageResponseProcessor<T> {
    @NonNull
    T processResponse(@NonNull StorageDataMessage dataMessage, @NonNull ApplicationResponse applicationResponse);
}
