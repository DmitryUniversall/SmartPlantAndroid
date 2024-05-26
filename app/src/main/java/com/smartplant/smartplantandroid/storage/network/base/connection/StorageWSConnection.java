package com.smartplant.smartplantandroid.storage.network.base.connection;

import static com.smartplant.smartplantandroid.utils.network.ApiHelper.getGson;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.smartplant.smartplantandroid.storage.models.StorageDataMessage;
import com.smartplant.smartplantandroid.storage.models.TransferResponseMSGType;
import com.smartplant.smartplantandroid.storage.network.base.StorageWSProcessor;
import com.smartplant.smartplantandroid.utils.AppLogger;
import com.smartplant.smartplantandroid.utils.data.json.JsonUtils;
import com.smartplant.smartplantandroid.utils.network.TransferResponse;

import java.util.ArrayList;
import java.util.UUID;

import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class StorageWSConnection extends WebSocketListener {
    protected static final @NonNull Gson _gson = getGson();

    protected final @NonNull ArrayList<StorageWSConnectHandler> _connectCallbackStack = new ArrayList<>();
    protected final @NonNull ArrayList<StorageWSDisconnectHandler> _disconnectCallbackStack = new ArrayList<>();
    protected final @NonNull ArrayList<StorageWSFailureHandler> _failureCallbackStack = new ArrayList<>();

    protected @Nullable WebSocket _webSocket;
    protected final @NonNull StorageWSProcessor _processor;
    protected final @NonNull String _id = UUID.randomUUID().toString();

    public StorageWSConnection(@NonNull StorageWSProcessor processor) {
        this._processor = processor;
    }

    protected void _callConnectStack(@NonNull WebSocket webSocket, @NonNull okhttp3.Response response) {
        this._connectCallbackStack.forEach(handler -> handler.onConnect(webSocket, response));
    }

    protected void _callDisconnectStack(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
        this._disconnectCallbackStack.forEach(handler -> handler.onDisconnect(webSocket, code, reason));
    }

    protected void _callFailureStack(@NonNull WebSocket webSocket, @NonNull Throwable throwable, okhttp3.Response response) {
        this._failureCallbackStack.forEach(handler -> handler.onFailure(webSocket, throwable, response));
    }

    public void whenConnected(@NonNull StorageWSConnectHandler handler) {
        _connectCallbackStack.add(handler);
    }

    public void whenDisconnected(@NonNull StorageWSDisconnectHandler handler) {
        _disconnectCallbackStack.add(handler);
    }

    public void whenFailure(@NonNull StorageWSFailureHandler handler) {
        _failureCallbackStack.add(handler);
    }

    @NonNull
    public String getId() {
        return _id;
    }

    @Override
    public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable throwable, okhttp3.Response response) {
        super.onFailure(webSocket, throwable, response);
        AppLogger.info("An WebSocket error occurred");
        this._callFailureStack(webSocket, throwable, response);
    }

    @Override
    public void onOpen(@NonNull WebSocket webSocket, @NonNull okhttp3.Response response) {
        super.onOpen(webSocket, response);
        this._webSocket = webSocket;
        AppLogger.info("WebSocket connection is established");
        this._callConnectStack(webSocket, response);
    }

    @Override
    public void onClosing(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
        super.onClosing(webSocket, code, reason);
        AppLogger.info("WebSocket connection is about to close");
        this._callDisconnectStack(webSocket, code, reason);
    }

    @Override
    public void onClosed(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
        super.onClosed(webSocket, code, reason);
        AppLogger.info("WebSocket connection is closed");
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

        if (msgType == TransferResponseMSGType.RESPONSE.getValue()) {
            TransferResponse transferResponse = JsonUtils.fromJsonWithNulls(data, TransferResponse.class, _gson);
            this._processor.processTransferResponse(transferResponse);
        } else if (msgType == TransferResponseMSGType.DATA.getValue()) {
            StorageDataMessage storageDataMessage = JsonUtils.fromJsonWithNulls(data, StorageDataMessage.class, _gson);
            this._processor.processStorageDataMessage(storageDataMessage);
        } else {
            AppLogger.warning("Ignoring unknown msg_type: %d", msgType);
        }
    }
}
