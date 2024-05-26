package com.smartplant.smartplantandroid.storage.network.base.connection;

import androidx.annotation.NonNull;

import okhttp3.WebSocket;

public interface StorageWSDisconnectHandler {
    void onDisconnect(@NonNull WebSocket webSocket, int code, @NonNull String reason);
}
