package com.smartplant.smartplantandroid.main.components.storage.internal_utils.processors.actions;

import androidx.annotation.NonNull;
import androidx.annotation.ReturnThis;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.gson.JsonSyntaxException;
import com.smartplant.smartplantandroid.core.data.json.JsonUtils;
import com.smartplant.smartplantandroid.core.logs.AppLogger;
import com.smartplant.smartplantandroid.main.components.storage.internal_utils.processors.StorageWSProcessor;
import com.smartplant.smartplantandroid.main.components.storage.models.StorageAction;
import com.smartplant.smartplantandroid.main.components.storage.models.StorageDataMessage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Request;

public class StorageWSActionProcessor extends StorageWSProcessor {
    // Reusable handlers
    protected final @NonNull ConcurrentHashMap<Integer, ConcurrentHashMap<String, StorageActionHandler>> _actionHandlers = new ConcurrentHashMap<>();  // Device Application Response Callback Stack

    public StorageWSActionProcessor(@NonNull Request initialRequest) {
        super(initialRequest);
    }

    protected void _callActionHandlers(@NonNull StorageDataMessage dataMessage, @NonNull StorageAction action) {
        Map<String, StorageActionHandler> handlersMap = _actionHandlers.get(action.getAction());
        if (handlersMap == null) {
            AppLogger.warning("Unprocessed action: %d", action.getAction());
            return;
        }

        for (StorageActionHandler handler : handlersMap.values()) {
            handler.onAction(dataMessage, action);
        }
    }

    protected void _processAction(@NonNull StorageDataMessage dataMessage, @NonNull StorageAction action) {
        _callActionHandlers(dataMessage, action);
    }

    @Override
    protected void _processDataMessage(@NonNull StorageDataMessage dataMessage) {
        super._processDataMessage(dataMessage);

        int dataType = dataMessage.getDataType();
        if (dataType == StorageDataMessage.DataType.REQUEST.getValue()) {
            StorageAction action;
            try {
                action = JsonUtils.fromJsonWithNulls(dataMessage.getData(), StorageAction.class, _gson);
            } catch (JsonSyntaxException e) {
                return;
            }

            this._processAction(dataMessage, action);
        }
    }

    @ReturnThis
    @CanIgnoreReturnValue
    public StorageWSActionProcessor onAction(int action, @NonNull String name, @NonNull StorageActionHandler handler) {
        AppLogger.info("Registering handler for action %d", action);
        ConcurrentHashMap<String, StorageActionHandler> handlersMap;

        if (!this._actionHandlers.containsKey(action)) {
            handlersMap = new ConcurrentHashMap<>();
            this._actionHandlers.put(action, handlersMap);
        } else {
            handlersMap = this._actionHandlers.get(action);
            assert handlersMap != null;
        }

        handlersMap.put(name, handler);
        return this;
    }

    public void removeActionHandler(int action, @NonNull String name) {
        ConcurrentHashMap<String, StorageActionHandler> handlersMap;
        if (!this._actionHandlers.containsKey(action)) return;

        handlersMap = this._actionHandlers.get(action);
        assert handlersMap != null;
        handlersMap.remove(name);
    }
}
