package com.smartplant.smartplantandroid.main.components.storage.internal_utils.connection;

import androidx.annotation.NonNull;

public interface StorageFailureCallback {
    void onFailure(@NonNull Throwable error, okhttp3.Response response);
}
