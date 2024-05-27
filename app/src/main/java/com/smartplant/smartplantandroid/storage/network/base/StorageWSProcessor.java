package com.smartplant.smartplantandroid.storage.network.base;

import androidx.annotation.NonNull;

import com.smartplant.smartplantandroid.storage.models.StorageDataMessage;
import com.smartplant.smartplantandroid.storage.network.base.connection.StorageWSConnection;
import com.smartplant.smartplantandroid.utils.network.ApplicationResponse;

import okhttp3.Request;

public abstract class StorageWSProcessor {
    private final StorageWSConnection _connection;

    public StorageWSProcessor(@NonNull Request initialRequest) {
        this._connection = _connect(initialRequest);
    }

    protected StorageWSConnection _connect(@NonNull Request initialRequest) {
        StorageWSConnection connection = new StorageWSConnection(initialRequest);
        connection.whenSAR(this::processSAR);
        connection.whenDataMessage(this::processDataMessage);
        connection.connect();
        return connection;
    }

    public StorageWSConnection getConnection() {
        return _connection;
    }

    public abstract void processSAR(@NonNull ApplicationResponse response);

    public abstract void processDataMessage(@NonNull StorageDataMessage dataMessage);
}
