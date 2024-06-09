package com.smartplant.smartplantandroid.main.components.storage.internal_utils.connection;

import static com.smartplant.smartplantandroid.core.data.json.JsonUtils.getGson;
import static com.smartplant.smartplantandroid.core.network.ApiUtils.getOkHttpClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.ReturnThis;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.smartplant.smartplantandroid.core.data.json.JsonUtils;
import com.smartplant.smartplantandroid.core.logs.AppLogger;
import com.smartplant.smartplantandroid.main.components.storage.models.StorageDataMessage;
import com.smartplant.smartplantandroid.main.components.storage.models.StorageWSMessage;

import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class StorageWSConnection extends WebSocketListener {
    // Utils
    protected static final @NonNull Gson _gson = getGson();
    protected static final @NonNull OkHttpClient _client = getOkHttpClient();

    // Disposable callbacks
    protected final @NonNull Queue<StorageConnectCallback> _connectCallbacks = new LinkedList<>();
    protected final @NonNull Queue<StorageDisconnectCallback> _disconnectCallbacks = new LinkedList<>();
    protected final @NonNull Queue<StorageFailureCallback> _failureCallbacks = new LinkedList<>();

    // Reusable handlers
    protected final @NonNull ConcurrentHashMap<String, StorageDataMessageHandler> _dataMessageHandlers = new ConcurrentHashMap<>();

    // Other
    protected boolean _connected = false;
    protected final @NonNull String _id = UUID.randomUUID().toString();
    protected final @NonNull Request _initialRequest;
    protected @Nullable WebSocket _webSocket;

    public StorageWSConnection(@NonNull Request initialRequest) {
        this._initialRequest = initialRequest;
    }

    protected void _callFailureCallbacks(@NonNull Throwable error, okhttp3.Response response) {
        if (this._failureCallbacks.isEmpty()) {
            AppLogger.error("Unprocessed storage ws connection error", error);
            return;
        }

        Throwable currentError = error;
        while (!this._failureCallbacks.isEmpty()) {
            StorageFailureCallback handler = this._failureCallbacks.poll();
            assert handler != null;

            try {
                handler.onFailure(currentError, response);
            } catch (Throwable e) {
                // Error occurred in last handler, so it will not be processed and will be lost
                if (this._failureCallbacks.isEmpty())
                    AppLogger.error("Unprocessed storage ws connection error", e);
                currentError = e;
            }
        }
    }

    protected void _callConnectCallbacks(@NonNull okhttp3.Response response) {
        if (this._connectCallbacks.isEmpty()) return;
        this._connectCallbacks.forEach(handler -> handler.onConnect(response));
        this._connectCallbacks.clear();
    }

    protected void _callDisconnectCallbacks(int code, @NonNull String reason) {
        if (this._disconnectCallbacks.isEmpty()) return;
        this._disconnectCallbacks.forEach(handler -> handler.onDisconnect(code, reason));
        this._disconnectCallbacks.clear();
    }

    protected void _callDataMessageHandlers(@NonNull StorageDataMessage dataMessage) {
        if (this._dataMessageHandlers.isEmpty()) {
            AppLogger.warning("Unprocessed data message (type: %d; from: %d)", dataMessage.getDataType(), dataMessage.getSenderId());
            return;
        }

        for (StorageDataMessageHandler handler : this._dataMessageHandlers.values()) {
            handler.onDataMessage(dataMessage);
        }
    }

    @NonNull
    public String getId() {
        return _id;
    }

    public void connect() {
        this._webSocket = _client.newWebSocket(_initialRequest, this);
    }

    public boolean isConnected() {
        return this._webSocket != null && _connected;
    }

    @ReturnThis
    @CanIgnoreReturnValue
    public StorageWSConnection connectedCallback(@NonNull StorageConnectCallback callback) {
        _connectCallbacks.add(callback);
        return this;
    }

    @ReturnThis
    @CanIgnoreReturnValue
    public StorageWSConnection disconnectedCallback(@NonNull StorageDisconnectCallback callback) {
        _disconnectCallbacks.add(callback);
        return this;
    }

    @ReturnThis
    @CanIgnoreReturnValue
    public StorageWSConnection failureCallback(@NonNull StorageFailureCallback callback) {
        _failureCallbacks.add(callback);
        return this;
    }

    @ReturnThis
    @CanIgnoreReturnValue
    public StorageWSConnection dataMessageHandler(@NonNull String name, @NonNull StorageDataMessageHandler handler) {
        this._dataMessageHandlers.put(name, handler);
        return this;
    }

    @ReturnThis
    @CanIgnoreReturnValue
    public StorageWSConnection removeDataMessageHandler(@NonNull String name) {
        this._dataMessageHandlers.remove(name);
        return this;
    }

    @Override
    public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable throwable, okhttp3.Response response) {
        super.onFailure(webSocket, throwable, response);
        AppLogger.info("An WebSocket error occurred");
        this._callFailureCallbacks(throwable, response);
    }

    @Override
    public void onOpen(@NonNull WebSocket webSocket, @NonNull okhttp3.Response response) {
        super.onOpen(webSocket, response);
        AppLogger.info("WebSocket connection is established");

        this._connected = true;
        this._callConnectCallbacks(response);
    }

    @Override
    public void onClosing(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
        super.onClosing(webSocket, code, reason);
        AppLogger.info("WebSocket connection is about to close");
    }

    @Override
    public void onClosed(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
        super.onClosed(webSocket, code, reason);
        AppLogger.info("WebSocket connection is closed");

        this._connected = false;
        this._callDisconnectCallbacks(code, reason);
    }

    @Override
    public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
        super.onMessage(webSocket, text);
        AppLogger.info("Received a message from the WebSocket server");

        JsonObject json;
        try {
            json = JsonParser.parseString(text).getAsJsonObject();
        } catch (JsonSyntaxException error) {
            AppLogger.warning("Ignoring invalid message from transfer WebSocket: %s", text);
            return;
        }

        JsonObject data = json.get("data").getAsJsonObject();
        int msgType = json.get("msg_type").getAsInt();
        if (msgType != StorageWSMessage.MSGType.DATA_MESSAGE.getValue()) {
            AppLogger.warning("Ignoring unknown msg_type: %d", msgType);
            return;
        }

        StorageDataMessage dataMessage = JsonUtils.fromJsonWithNulls(data, StorageDataMessage.class, _gson);
        this._callDataMessageHandlers(dataMessage);
    }

    public void close(int code, String reason) {
        if (!this.isConnected())
            throw new IllegalStateException("Unable to send storage request because websocket is no connected");

        assert this._webSocket != null;
        this._webSocket.close(code, reason);
    }
}
