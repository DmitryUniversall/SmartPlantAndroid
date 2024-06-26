package com.smartplant.smartplantandroid.main.components.storage.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.smartplant.smartplantandroid.core.callbacks.CallbackUtils;
import com.smartplant.smartplantandroid.core.logs.AppLogger;
import com.smartplant.smartplantandroid.core.network.http.http_api_request.HTTPApiRequest;
import com.smartplant.smartplantandroid.main.components.storage.internal_utils.StorageApiService;
import com.smartplant.smartplantandroid.main.components.storage.internal_utils.StorageWSService;
import com.smartplant.smartplantandroid.main.components.storage.internal_utils.processors.actions.StorageWSActionProcessor;
import com.smartplant.smartplantandroid.main.components.storage.models.write.StorageRequestPayload;
import com.smartplant.smartplantandroid.main.components.storage.models.write.StorageResponsePayload;

import java.util.concurrent.ConcurrentHashMap;

public class StorageRepositoryST {
    private static @Nullable StorageRepositoryST _instance;
    private final StorageWSService _WSService;
    private final StorageApiService _apiService;

    // Reusable handlers
    protected final @NonNull ConcurrentHashMap<String, Runnable> _processorCreateHandlers = new ConcurrentHashMap<>();

    public static synchronized void createInstance() {
        if (_instance != null)
            throw new RuntimeException("StorageRepositoryST has already been initialized");
        _instance = new StorageRepositoryST();
    }

    public static synchronized StorageRepositoryST getInstance() {
        if (_instance == null)
            throw new RuntimeException("StorageRepositoryST has not been initialized");
        return _instance;
    }

    private StorageRepositoryST() {
        this._WSService = new StorageWSService();
        this._apiService = new StorageApiService();
    }

    private void _callProcessorCreateHandlers() {
        CallbackUtils.callRunnableCallbacks(this._processorCreateHandlers.values());
    }

    public StorageWSActionProcessor connect() {
        AppLogger.info("Connecting storage WS");
        StorageWSActionProcessor processor = _WSService.connect();
        _callProcessorCreateHandlers();
        return processor;
    }

    public void disconnect(int code, @NonNull String reason) {
        AppLogger.info("Disconnecting storage WS");

        StorageWSActionProcessor processor = _WSService.getProcessor();
        if (processor == null) {
            AppLogger.warning("Unable to disconnect storage WS: Not connected");
            return;
        }

        processor.disconnect(code, reason);
    }

    public boolean isConnected() {
        StorageWSActionProcessor processor = _WSService.getProcessor();
        if (processor == null) return false;
        return processor.isConnected();
    }

    public void onProcessorCreate(@NonNull String name, @NonNull Runnable handler) {
        this._processorCreateHandlers.put(name, handler);
    }

    public void removeProcessorCreateHandler(@NonNull String name) {
        this._processorCreateHandlers.remove(name);
    }

    @Nullable
    public StorageWSActionProcessor getProcessorOrNull() {
        return this._WSService.getProcessor();
    }

    public StorageWSActionProcessor getProcessor() {
        StorageWSActionProcessor actionProcessor = this.getProcessorOrNull();
        if (actionProcessor == null)
            throw new IllegalStateException("Unable to get processor: not connected");
        return actionProcessor;
    }

    public HTTPApiRequest<String> writeRequest(@NonNull StorageRequestPayload payload) {
        return this._apiService.writeRequest(payload);
    }

    public HTTPApiRequest<Void> writeResponse(@NonNull StorageResponsePayload payload) {
        return this._apiService.writeResponse(payload);
    }
}
