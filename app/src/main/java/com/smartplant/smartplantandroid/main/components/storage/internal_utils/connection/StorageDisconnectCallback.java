package com.smartplant.smartplantandroid.main.components.storage.internal_utils.connection;

import androidx.annotation.NonNull;

public interface StorageDisconnectCallback {
    void onDisconnect(int code, @NonNull String reason);
}
