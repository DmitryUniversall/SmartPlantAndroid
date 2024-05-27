package com.smartplant.smartplantandroid.storage.network.base.connection;

import androidx.annotation.NonNull;

public interface StorageDisconnectHandler {
    void onDisconnect(int code, @NonNull String reason);
}
