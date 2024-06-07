package com.smartplant.smartplantandroid.main.components.storage.internal_utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.smartplant.smartplantandroid.core.logs.AppLogger;
import com.smartplant.smartplantandroid.core.network.ApiUtils;
import com.smartplant.smartplantandroid.main.components.auth.exceptions.UnauthorizedException;
import com.smartplant.smartplantandroid.main.components.storage.internal_utils.processors.actions.StorageWSActionProcessor;

import okhttp3.Request;
import okhttp3.Response;

public class StorageWSService {
    private final @NonNull String _apiStorageBase;
    private @Nullable StorageWSActionProcessor _processor;

    public StorageWSService() {
        this._apiStorageBase = ApiUtils.getBaseURL("ws") + "storage/";
    }

    @NonNull
    public StorageWSActionProcessor connect() throws UnauthorizedException {
        if (this._processor != null) {
            AppLogger.info("Closing old websocket; Initializing new");
            this.disconnect();
        }

        Request request = ApiUtils.getAuthorizedRequestBuilder().url(this._apiStorageBase).build();
        this._processor = new StorageWSActionProcessor(request);
        this._processor.onConnectionFailure(this::_handleFailure).onDisconnected(this::_handleDisconnect);
        return this._processor;
    }

    public void disconnect() {
        if (this._processor == null) return;
        this._processor.disconnect(1000, "Normal disconnect");
        this._processor = null;
    }

    @Nullable
    public StorageWSActionProcessor getProcessor() {
        return _processor;
    }

    private void _handleFailure(Throwable throwable, Response response) {
        AppLogger.error("Websocket connection failed", throwable);
    }

    private void _handleDisconnect(int code, String reason) {  // TODO: reconnect on session end
        AppLogger.debug("Websocket disconnected with code %d (%s)", code, reason);
    }
}
