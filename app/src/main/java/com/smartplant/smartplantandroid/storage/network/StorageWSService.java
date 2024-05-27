package com.smartplant.smartplantandroid.storage.network;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.smartplant.smartplantandroid.auth.exceptions.UnauthorizedException;
import com.smartplant.smartplantandroid.storage.network.actions.ActionProcessor;
import com.smartplant.smartplantandroid.storage.network.base.connection.StorageWSConnection;
import com.smartplant.smartplantandroid.utils.AppLogger;
import com.smartplant.smartplantandroid.utils.network.ApiHelper;
import com.smartplant.smartplantandroid.utils.settings.ProjectSettingsST;

import okhttp3.Request;
import okhttp3.Response;

public class StorageWSService {
    private final @NonNull ProjectSettingsST _projectSettings;
    private final @NonNull String _apiStorageBase;
    private @Nullable ActionProcessor _processor;

    public StorageWSService() {
        this._apiStorageBase = ApiHelper.getBaseURL("ws") + "storage/";
        this._projectSettings = ProjectSettingsST.getInstance();
    }

    @NonNull
    private String getMyStorageURL() {
        return this._apiStorageBase + _projectSettings.getTSAppDeviceID() + "/";  // TODO: Will be changed to user id in the future
    }

    @NonNull
    public ActionProcessor connect() throws UnauthorizedException {
        if (this._processor != null) {
            AppLogger.info("Closing old websocket; Initializing new");
            this.disconnect();
        }

        Request request = ApiHelper.getAuthorizedRequestBuilder().url(getMyStorageURL()).build();
        this._processor = new ActionProcessor(request);

        StorageWSConnection connection = _processor.getConnection();
        connection.whenFailure(this::_handleFailure).whenDisconnected(this::_handleDisconnect);
        connection.connect();

        return this._processor;
    }

    public void disconnect() {
        if (this._processor == null) return;
        this._processor.disconnect(1000, "Normal disconnect");
        this._processor = null;
    }

    @Nullable
    public ActionProcessor getProcessor() {
        return _processor;
    }

    private void _handleFailure(Throwable throwable, Response response) {
        AppLogger.error("Websocket connection failed", throwable);
    }

    private void _handleDisconnect(int code, String reason) {
        AppLogger.debug("Websocket disconnected with code %d (%s)", code, reason);  // TODO: reconnect on session end
    }
}
