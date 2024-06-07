package com.smartplant.smartplantandroid.main.components.storage.utils;

import androidx.annotation.Nullable;

import com.smartplant.smartplantandroid.core.models.ApplicationResponse;
import com.smartplant.smartplantandroid.main.components.storage.models.StorageDataMessage;

public class StorageRequestResult<T> {
    public final boolean success;
    public final @Nullable T result;
    public final @Nullable ApplicationResponse response;
    public final @Nullable StorageDataMessage dataMessage;
    public final @Nullable Throwable error;

    public StorageRequestResult(
            boolean success,
            @Nullable T result,
            @Nullable ApplicationResponse response,
            @Nullable StorageDataMessage dataMessage,
            @Nullable Throwable error
    ) {
        this.success = success;
        this.result = result;
        this.response = response;
        this.dataMessage = dataMessage;
        this.error = error;
    }
}
