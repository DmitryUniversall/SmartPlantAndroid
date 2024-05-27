package com.smartplant.smartplantandroid.storage.network.base.connection;

import androidx.annotation.NonNull;

public interface StorageConnectHandler {
    void onConnect(@NonNull okhttp3.Response response);
}
