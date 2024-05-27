package com.smartplant.smartplantandroid.storage.network.base.connection;

import androidx.annotation.NonNull;

public interface StorageFailureHandler {
    void onFailure(@NonNull Throwable throwable, okhttp3.Response response);
}
