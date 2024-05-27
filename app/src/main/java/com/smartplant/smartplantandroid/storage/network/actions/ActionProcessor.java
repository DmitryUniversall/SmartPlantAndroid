package com.smartplant.smartplantandroid.storage.network.actions;

import static com.smartplant.smartplantandroid.utils.network.ApiHelper.getGson;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.ReturnThis;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.smartplant.smartplantandroid.storage.models.StorageAction;
import com.smartplant.smartplantandroid.storage.models.StorageDataMessage;
import com.smartplant.smartplantandroid.storage.models.StorageRequest;
import com.smartplant.smartplantandroid.storage.network.actions.request.ActionRequest;
import com.smartplant.smartplantandroid.storage.network.base.StorageWSProcessor;
import com.smartplant.smartplantandroid.utils.AppLogger;
import com.smartplant.smartplantandroid.utils.data.json.JsonUtils;
import com.smartplant.smartplantandroid.utils.network.ApplicationResponse;
import com.smartplant.smartplantandroid.utils.network.ApplicationResponseHandler;

import java.util.ArrayList;

import okhttp3.Request;

public class ActionProcessor extends StorageWSProcessor {
    protected static final @NonNull Gson _gson = getGson();

    private final @NonNull ArrayList<ApplicationResponseHandler> _SARCallbackStack = new ArrayList<>();  // Server Application Response Callback Stack
    private final @NonNull ArrayList<StorageActionHandler> _actionCallbackStack = new ArrayList<>();
    private final @NonNull ArrayList<StorageDARHandler> _DARCallbackStack = new ArrayList<>();  // Device Application Response Callback Stack

    public ActionProcessor(@NonNull Request initialRequest) {
        super(initialRequest);
    }

    public void processSAR(@NonNull ApplicationResponse response) {
        AppLogger.debug("Got application response");
        _callSARCallbackStack(response);
    }

    public void processDataMessage(@NonNull StorageDataMessage dataMessage) {
        AppLogger.debug("Got storage data message");

        int dataType = dataMessage.getDataType();
        if (dataType == StorageDataMessage.MessageDataType.REQUEST.getValue()) {
            StorageAction action = JsonUtils.fromJsonWithNulls(dataMessage.getData(), StorageAction.class, _gson);
            this._processAction(dataMessage, action);
        } else if (dataType == StorageDataMessage.MessageDataType.RESPONSE.getValue()) {
            ApplicationResponse response = JsonUtils.fromJsonWithNulls(dataMessage.getData(), ApplicationResponse.class, _gson);
            this._processDeviceApplicationResponse(dataMessage, response);
        }
    }

    protected void _processAction(@NonNull StorageDataMessage dataMessage, @NonNull StorageAction action) {
        _callActionCallbackStack(dataMessage, action);
    }

    protected void _processDeviceApplicationResponse(@NonNull StorageDataMessage dataMessage, @NonNull ApplicationResponse response) {
        _callDARCallbackStack(dataMessage, response);
    }

    protected void _callActionCallbackStack(@NonNull StorageDataMessage dataMessage, @NonNull StorageAction action) {
        if (this._actionCallbackStack.isEmpty()) {
            AppLogger.warning("Unprocessed action: %d", action.getAction());
            return;
        }

        for (StorageActionHandler handler : this._actionCallbackStack) {
            handler.onAction(dataMessage, action);
        }
    }

    protected void _callSARCallbackStack(@NonNull ApplicationResponse response) {
        if (this._SARCallbackStack.isEmpty()) return;

        for (ApplicationResponseHandler handler : this._SARCallbackStack) {
            handler.onApplicationResponse(response);
        }
    }

    protected void _callDARCallbackStack(@NonNull StorageDataMessage dataMessage, @NonNull ApplicationResponse response) {
        if (this._DARCallbackStack.isEmpty()) return;

        for (StorageDARHandler handler : this._DARCallbackStack) {
            handler.onDeviceApplicationResponse(dataMessage, response);
        }
    }

    @ReturnThis
    @CanIgnoreReturnValue
    public ActionProcessor onAction(@NonNull StorageActionHandler handler) {
        this._actionCallbackStack.add(handler);
        return this;
    }

    @ReturnThis
    @CanIgnoreReturnValue
    public ActionProcessor onServerApplicationResponse(@NonNull ApplicationResponseHandler handler) {
        this._SARCallbackStack.add(handler);
        return this;
    }

    @ReturnThis
    @CanIgnoreReturnValue
    public ActionProcessor onDeviceApplicationResponse(@NonNull StorageDARHandler handler) {
        this._DARCallbackStack.add(handler);
        return this;
    }


    public void sendActionResponse(@NonNull ApplicationResponse response, @NonNull String targetDeviceId, @Nullable String messageId) {
        JsonObject data = _gson.toJsonTree(response).getAsJsonObject();
        this.getConnection().sendStorageRequest(new StorageRequest(
                StorageRequest.StorageRequestType.ENQUEUE_RESPONSE.getValue(),
                targetDeviceId,
                messageId,
                data
        ));
    }

    public void sendStorageRequest(@NonNull StorageRequest request) {
        this.getConnection().sendStorageRequest(request);
    }

    public ActionRequest createActionRequest(@NonNull StorageAction action, @NonNull String targetDeviceId) {
        return new ActionRequest(this, action, targetDeviceId);
    }

    public void disconnect(int code, String reason) {
        this.getConnection().close(code, reason);
    }
}
