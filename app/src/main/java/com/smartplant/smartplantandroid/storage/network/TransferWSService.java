package com.smartplant.smartplantandroid.storage.network;

import static com.smartplant.smartplantandroid.utils.network.ApiHelper.getOkHttpClient;

import android.util.Pair;

import androidx.annotation.NonNull;

import com.smartplant.smartplantandroid.auth.exceptions.UnauthorizedException;
import com.smartplant.smartplantandroid.storage.network.base.StorageWSProcessor;
import com.smartplant.smartplantandroid.storage.network.base.connection.StorageWSConnection;
import com.smartplant.smartplantandroid.utils.AppLogger;
import com.smartplant.smartplantandroid.utils.network.ApiHelper;
import com.smartplant.smartplantandroid.utils.settings.ProjectSettingsST;

import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class TransferWSService extends WebSocketListener {
    private static final @NonNull OkHttpClient _client = getOkHttpClient();

    private final @NonNull HashMap<String, Pair<WebSocket, StorageWSConnection>> _connections = new HashMap<>();
    private final @NonNull ProjectSettingsST _projectSettings;
    private final @NonNull String _apiStorageBase;

    public TransferWSService(@NonNull StorageWSProcessor processor) {
        this._apiStorageBase = ApiHelper.getBaseURL("ws") + "storage/";
        this._projectSettings = ProjectSettingsST.getInstance();
    }

    @NonNull
    private String getMyStorageURL() {
        return this._apiStorageBase + _projectSettings.getTSAppDeviceID();  // TODO: Will be changed to user id in the future
    }

    @NonNull
    public StorageWSConnection connect(@NonNull StorageWSProcessor processor) throws UnauthorizedException {
        StorageWSConnection connection = new StorageWSConnection(processor);
        connection.whenDisconnected((websocket, code, reason) -> {
            AppLogger.debug("Websocket disconnected with code %d (%s)", code, reason);  // TODO: reconnect on session end
        });

        Request request = ApiHelper.getAuthorizedRequestBuilder().url(getMyStorageURL()).build();
        WebSocket webSocket = _client.newWebSocket(request, connection);

        _connections.put(connection.getId(), new Pair<>(webSocket, connection));
        return connection;
    }

    public void disconnect(@NonNull String connectionId) {
        if (!_connections.containsKey(connectionId)) return;

        Pair<WebSocket, StorageWSConnection> connectionData = _connections.get(connectionId);
        assert connectionData != null;

        connectionData.first.close(1000, "Normal disconnect");
    }
}
