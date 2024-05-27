package com.smartplant.smartplantandroid.storage.network.actions.request;

import static com.smartplant.smartplantandroid.utils.network.ApiHelper.getGson;

import androidx.annotation.NonNull;
import androidx.annotation.ReturnThis;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.smartplant.smartplantandroid.storage.models.StorageAction;
import com.smartplant.smartplantandroid.storage.models.StorageDataMessage;
import com.smartplant.smartplantandroid.storage.models.StorageRequest;
import com.smartplant.smartplantandroid.storage.network.actions.ActionProcessor;
import com.smartplant.smartplantandroid.utils.AppLogger;
import com.smartplant.smartplantandroid.utils.network.ApplicationResponse;
import com.smartplant.smartplantandroid.utils.network.ApplicationResponseException;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class ActionRequest {
    private static final @NonNull Gson _gson = getGson();

    private final @NonNull ArrayList<ActionRequestSuccessHandler> _successCallbackStack = new ArrayList<>();
    private final @NonNull ArrayList<ActionRequestFailureHandler> _failureCallbackStack = new ArrayList<>();

    private final @NonNull ActionProcessor _processor;
    private final @NonNull StorageAction _action;
    private final @NonNull String _targetDeviceId;
    private final @NonNull String _messageID;

    public ActionRequest(@NonNull ActionProcessor processor, @NonNull StorageAction action, @NonNull String targetDeviceId) {
        this._processor = processor;
        this._action = action;
        this._targetDeviceId = targetDeviceId;
        this._messageID = UUID.randomUUID().toString();
        this._selfRegister();
    }

    private void _selfRegister() {
        this._processor.onServerApplicationResponse(this::onServerApplicationResponse);  // TODO: Delete this callback after request ended
        this._processor.onDeviceApplicationResponse(this::onDeviceApplicationResponse);  // TODO: Delete this callback after request ended
    }

    protected void _callSuccessCallStack(@NonNull ApplicationResponse response, @NonNull StorageDataMessage dataMessage) {
        _successCallbackStack.forEach((handler) -> handler.onSuccess(response, dataMessage));
    }

    protected void _callFailureCallStack(@NonNull Throwable error) {
        if (_failureCallbackStack.isEmpty()) {
            AppLogger.error("Unprocessed request error", error);
            return;
        }

        Throwable currentError = error;

        int stackSize = _failureCallbackStack.size();
        for (int index = 0; index < stackSize; index++) {
            ActionRequestFailureHandler handler = _failureCallbackStack.get(index);

            try {
                handler.onFailure(currentError);
            } catch (Throwable e) {
                // Error occurred in last handler, so it will not be processed and will be lost
                if (index == stackSize - 1) AppLogger.error("Unprocessed request error", e);
                currentError = e;
            }
        }
    }

    private void _processEnqueued(@NonNull ApplicationResponse response) throws ApplicationResponseException {
        if (response.isOk()) {  // TODO: Make timeout
            AppLogger.debug("ActionRequest %s enqueued", _messageID);
        } else {
            throw new ApplicationResponseException(response);
        }
    }

    private void _processResponded(@NonNull StorageDataMessage dataMessage, @NonNull ApplicationResponse response) throws ApplicationResponseException {
        if (!Objects.equals(dataMessage.getMessageId(), this._messageID)) return;

        if (response.isOk()) {
            AppLogger.debug("ActionRequest %s responded with success", _messageID);
            this._callSuccessCallStack(response, dataMessage);
        } else {
            throw new ApplicationResponseException(response);
        }
    }

    private void onServerApplicationResponse(@NonNull ApplicationResponse response) {
        try {
            _processEnqueued(response);
        } catch (ApplicationResponseException error) {
            _callFailureCallStack(error);
        }
    }

    private void onDeviceApplicationResponse(@NonNull StorageDataMessage dataMessage, @NonNull ApplicationResponse response) {
        try {
            _processResponded(dataMessage, response);
        } catch (ApplicationResponseException error) {
            _callFailureCallStack(error);
        }
    }

    @ReturnThis
    public ActionRequest onSuccess(ActionRequestSuccessHandler handler) {
        this._successCallbackStack.add(handler);
        return this;
    }

    @ReturnThis
    public ActionRequest onFailure(ActionRequestFailureHandler handler) {
        this._failureCallbackStack.add(handler);
        return this;
    }

    public void send() {
        JsonObject data = _gson.toJsonTree(this._action).getAsJsonObject();
        StorageRequest request = new StorageRequest(
                StorageRequest.StorageRequestType.ENQUEUE_REQUEST.getValue(),
                this._targetDeviceId,
                this._messageID,
                data
        );

        this._processor.sendStorageRequest(request);
    }
}
