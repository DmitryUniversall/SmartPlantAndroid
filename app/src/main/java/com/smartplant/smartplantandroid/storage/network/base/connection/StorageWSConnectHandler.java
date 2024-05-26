package com.smartplant.smartplantandroid.storage.network.base.connection;

import androidx.annotation.NonNull;

import okhttp3.WebSocket;

public interface StorageWSConnectHandler {
    void onConnect(@NonNull WebSocket webSocket, @NonNull okhttp3.Response response);
}
