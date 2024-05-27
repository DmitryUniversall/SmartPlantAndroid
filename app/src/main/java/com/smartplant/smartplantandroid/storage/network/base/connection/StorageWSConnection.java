package com.smartplant.smartplantandroid.storage.network.base.connection;

import static com.smartplant.smartplantandroid.utils.network.ApiHelper.getGson;
import static com.smartplant.smartplantandroid.utils.network.ApiHelper.getOkHttpClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.ReturnThis;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.smartplant.smartplantandroid.storage.models.StorageDataMessage;
import com.smartplant.smartplantandroid.storage.models.StorageMessage;
import com.smartplant.smartplantandroid.storage.models.StorageRequest;
import com.smartplant.smartplantandroid.utils.AppLogger;
import com.smartplant.smartplantandroid.utils.data.json.JsonUtils;
import com.smartplant.smartplantandroid.utils.network.ApplicationResponse;
import com.smartplant.smartplantandroid.utils.network.ApplicationResponseHandler;

import java.util.ArrayList;
import java.util.UUID;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class StorageWSConnection extends WebSocketListener {
    protected static final @NonNull Gson _gson = getGson();
    protected static final @NonNull OkHttpClient _client = getOkHttpClient();

    protected final @NonNull ArrayList<StorageConnectHandler> _connectCallbackStack = new ArrayList<>();
    protected final @NonNull ArrayList<StorageDisconnectHandler> _disconnectCallbackStack = new ArrayList<>();
    protected final @NonNull ArrayList<StorageFailureHandler> _failureCallbackStack = new ArrayList<>();
    protected final @NonNull ArrayList<StorageDataMessageHandler> _dataMessageCallbackStack = new ArrayList<>();
    protected final @NonNull ArrayList<ApplicationResponseHandler> _SARCallbackStack = new ArrayList<>();  // Server Application Response Callback Stack

    protected boolean _connected = false;
    protected @NonNull Request _initialRequest;
    protected final @NonNull String _id = UUID.randomUUID().toString();
    protected @Nullable WebSocket _webSocket;

    public StorageWSConnection(@NonNull Request initialRequest) {
        this._initialRequest = initialRequest;
    }

    public void connect() {
        this._webSocket = _client.newWebSocket(_initialRequest, this);
    }

    public boolean isConnected() {
        return this._webSocket != null && _connected;
    }

    protected void _callFailureStack(@NonNull Throwable throwable, okhttp3.Response response) {
        if (this._failureCallbackStack.isEmpty()) {
            AppLogger.error("Unprocessed storage WS connection failure", throwable);
            return;
        }

        this._failureCallbackStack.forEach(handler -> handler.onFailure(throwable, response));
//        this._failureCallbackStack.clear();  // Connection failed. We can delete this callbacks because StorageWSConnection is not reusable
    }

    protected void _callConnectStack(@NonNull okhttp3.Response response) {
        if (this._connectCallbackStack.isEmpty()) return;
        this._connectCallbackStack.forEach(handler -> handler.onConnect(response));
//        this._connectCallbackStack.clear();  // Connection established. We can delete this callbacks because StorageWSConnection is not reusable
    }

    protected void _callDisconnectStack(int code, @NonNull String reason) {
        if (this._disconnectCallbackStack.isEmpty()) return;
        this._disconnectCallbackStack.forEach(handler -> handler.onDisconnect(code, reason));
//        this._disconnectCallbackStack.clear();  // Disconnected. We can delete this callbacks because StorageWSConnection is not reusable
    }

    protected void _callSARStack(@NonNull ApplicationResponse response) {
        if (this._SARCallbackStack.isEmpty()) return;
        this._SARCallbackStack.forEach(handler -> handler.onApplicationResponse(response));
    }

    protected void _callDataMessageStack(@NonNull StorageDataMessage dataMessage) {
        if (this._dataMessageCallbackStack.isEmpty()) {
            AppLogger.warning("Unprocessed data message: %s", dataMessage.getMessageId());
            return;
        }

        this._dataMessageCallbackStack.forEach(handler -> handler.onDataMessage(dataMessage));
    }

    public void sendStorageRequest(StorageRequest request) {
        if (!this.isConnected())
            throw new IllegalStateException("Unable to send storage request because websocket is no connected");
        String serializedRequest = _gson.toJson(request);

        assert this._webSocket != null;
        this._webSocket.send(serializedRequest);
    }

    @ReturnThis
    @CanIgnoreReturnValue
    public StorageWSConnection whenConnected(@NonNull StorageConnectHandler handler) {
        _connectCallbackStack.add(handler);
        return this;
    }

    @ReturnThis
    @CanIgnoreReturnValue
    public StorageWSConnection whenDisconnected(@NonNull StorageDisconnectHandler handler) {
        _disconnectCallbackStack.add(handler);
        return this;
    }

    @ReturnThis
    @CanIgnoreReturnValue
    public StorageWSConnection whenFailure(@NonNull StorageFailureHandler handler) {
        _failureCallbackStack.add(handler);
        return this;
    }

    @ReturnThis
    @CanIgnoreReturnValue
    public StorageWSConnection whenSAR(@NonNull ApplicationResponseHandler handler) {
        this._SARCallbackStack.add(handler);
        return this;
    }

    @ReturnThis
    @CanIgnoreReturnValue
    public StorageWSConnection whenDataMessage(@NonNull StorageDataMessageHandler handler) {
        this._dataMessageCallbackStack.add(handler);
        return this;
    }

    @NonNull
    public String getId() {
        return _id;
    }

    @Override
    public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable throwable, okhttp3.Response response) {
        super.onFailure(webSocket, throwable, response);
        AppLogger.info("An WebSocket error occurred");
        this._callFailureStack(throwable, response);
    }

    @Override
    public void onOpen(@NonNull WebSocket webSocket, @NonNull okhttp3.Response response) {
        super.onOpen(webSocket, response);
        AppLogger.info("WebSocket connection is established");

        this._connected = true;
        this._callConnectStack(response);
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
        this._callDisconnectStack(code, reason);
    }

    @Override
    public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
        super.onMessage(webSocket, text);
        AppLogger.info("Received a message from the WebSocket server");

        JsonObject json;
        try {
            json = JsonParser.parseString(text).getAsJsonObject();
        } catch (JsonSyntaxException error) {
            AppLogger.warning("Ignoring invalid message from transfer WebSocket");
            return;
        }

        JsonObject data = json.get("data").getAsJsonObject();
        int msgType = json.get("msg_type").getAsInt();

        if (msgType == StorageMessage.StorageResponseMSGType.APPLICATION_RESPONSE.getValue()) {
            ApplicationResponse applicationResponse = JsonUtils.fromJsonWithNulls(data, ApplicationResponse.class, _gson);
            this._callSARStack(applicationResponse);
        } else if (msgType == StorageMessage.StorageResponseMSGType.DATA_MESSAGE.getValue()) {
            StorageDataMessage dataMessage = JsonUtils.fromJsonWithNulls(data, StorageDataMessage.class, _gson);
            this._callDataMessageStack(dataMessage);
        } else {
            AppLogger.warning("Ignoring unknown msg_type: %d", msgType);
        }
    }

    public void close(int code, String reason) {
        if (!this.isConnected())
            throw new IllegalStateException("Unable to send storage request because websocket is no connected");

        assert this._webSocket != null;
        this._webSocket.close(code, reason);
    }
}
