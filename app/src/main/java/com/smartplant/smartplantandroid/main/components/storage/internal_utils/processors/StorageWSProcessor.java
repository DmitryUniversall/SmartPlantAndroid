package com.smartplant.smartplantandroid.main.components.storage.internal_utils.processors;

import static com.smartplant.smartplantandroid.core.data.json.JsonUtils.getGson;

import androidx.annotation.NonNull;
import androidx.annotation.ReturnThis;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.gson.Gson;
import com.smartplant.smartplantandroid.core.data.json.JsonUtils;
import com.smartplant.smartplantandroid.core.models.ApplicationResponse;
import com.smartplant.smartplantandroid.main.components.storage.internal_utils.connection.StorageConnectCallback;
import com.smartplant.smartplantandroid.main.components.storage.internal_utils.connection.StorageDisconnectCallback;
import com.smartplant.smartplantandroid.main.components.storage.internal_utils.connection.StorageConnectFailureCallback;
import com.smartplant.smartplantandroid.main.components.storage.internal_utils.connection.StorageWSConnection;
import com.smartplant.smartplantandroid.main.components.storage.utils.storage_request.StorageRequest;
import com.smartplant.smartplantandroid.main.components.storage.models.StorageDataMessage;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Request;

public class StorageWSProcessor {
    // Utils
    protected static final @NonNull Gson _gson = getGson();

    // Reusable handlers
    protected final @NonNull ConcurrentHashMap<String, StorageDARHandler> _DARHandlers = new ConcurrentHashMap<>();  // Device Application Response Callback Stack

    // Other
    protected final @NonNull String _id = UUID.randomUUID().toString();
    protected final StorageWSConnection _connection;

    public StorageWSProcessor(@NonNull Request initialRequest) {
        this._connection = _connect(initialRequest);
    }

    protected StorageWSConnection _connect(@NonNull Request initialRequest) {
        StorageWSConnection connection = new StorageWSConnection(initialRequest);
        connection.dataMessageHandler(String.format("WSProcessor_%s", _id), this::_processDataMessage);
        connection.connect();
        return connection;
    }

    protected void _callDARHandlers(@NonNull StorageDataMessage dataMessage, @NonNull ApplicationResponse response) {
        if (this._DARHandlers.isEmpty()) return;

        for (StorageDARHandler handler : this._DARHandlers.values()) {
            handler.onDeviceApplicationResponse(dataMessage, response);
        }
    }

    protected void _processDAR(@NonNull StorageDataMessage dataMessage, @NonNull ApplicationResponse response) {
        _callDARHandlers(dataMessage, response);
    }

    protected void _processDataMessage(@NonNull StorageDataMessage dataMessage) {
        int dataType = dataMessage.getDataType();
        if (dataType == StorageDataMessage.DataType.RESPONSE.getValue()) {
            ApplicationResponse response = JsonUtils.fromJsonWithNulls(dataMessage.getData(), ApplicationResponse.class, _gson);
            this._processDAR(dataMessage, response);
        }
    }

    protected StorageWSConnection getConnection() {
        return _connection;
    }

    public String getId() {
        return _id;
    }

    @ReturnThis
    @CanIgnoreReturnValue
    public StorageWSProcessor onConnected(@NonNull StorageConnectCallback callback) {
        this._connection.connectedCallback(callback);
        return this;
    }

    @ReturnThis
    @CanIgnoreReturnValue
    public StorageWSProcessor onConnectionFailure(@NonNull StorageConnectFailureCallback callback) {
        this._connection.failureCallback(callback);
        return this;
    }

    @ReturnThis
    @CanIgnoreReturnValue
    public StorageWSProcessor onDisconnected(@NonNull StorageDisconnectCallback callback) {
        this._connection.disconnectedCallback(callback);
        return this;
    }

    @ReturnThis
    @CanIgnoreReturnValue
    public StorageWSProcessor onDAR(@NonNull String name, @NonNull StorageDARHandler handler) {
        this._DARHandlers.put(name, handler);
        return this;
    }

    @ReturnThis
    @CanIgnoreReturnValue
    public StorageWSProcessor removeDARHandler(@NonNull String name) {
        this._DARHandlers.remove(name);
        return this;
    }

    public void disconnect(int code, String reason) {
        this.getConnection().close(code, reason);
    }

    public <T> StorageRequest.Builder<T> getStorageRequestBuilder() {
        return new StorageRequest.Builder<>();
    }
}
