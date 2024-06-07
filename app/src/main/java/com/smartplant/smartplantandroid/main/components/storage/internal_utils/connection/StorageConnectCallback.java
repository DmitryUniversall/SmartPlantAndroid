package com.smartplant.smartplantandroid.main.components.storage.internal_utils.connection;

import androidx.annotation.NonNull;

public interface StorageConnectCallback {
    void onConnect(@NonNull okhttp3.Response response);
}
