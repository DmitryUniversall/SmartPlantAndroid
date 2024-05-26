package com.smartplant.smartplantandroid.storage.network.base.connection;

import androidx.annotation.NonNull;

import okhttp3.WebSocket;

public interface StorageWSFailureHandler {
    void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable throwable, okhttp3.Response response);
}
