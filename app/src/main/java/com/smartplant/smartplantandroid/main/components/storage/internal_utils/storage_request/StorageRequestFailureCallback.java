package com.smartplant.smartplantandroid.main.components.storage.internal_utils.storage_request;

import androidx.annotation.NonNull;

public interface StorageRequestFailureCallback {
    void onFailure(@NonNull Throwable error);
}
