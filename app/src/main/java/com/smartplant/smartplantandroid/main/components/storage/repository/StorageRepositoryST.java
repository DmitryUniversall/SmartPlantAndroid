package com.smartplant.smartplantandroid.main.components.storage.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.smartplant.smartplantandroid.core.network.http.http_api_request.HTTPApiRequest;
import com.smartplant.smartplantandroid.main.components.storage.internal_utils.StorageApiService;
import com.smartplant.smartplantandroid.main.components.storage.internal_utils.StorageWSService;
import com.smartplant.smartplantandroid.main.components.storage.internal_utils.processors.actions.StorageWSActionProcessor;
import com.smartplant.smartplantandroid.main.components.storage.models.write.StorageRequestPayload;
import com.smartplant.smartplantandroid.main.components.storage.models.write.StorageResponsePayload;

public class StorageRepositoryST {
    private static @Nullable StorageRepositoryST _instance;
    private final StorageWSService _WSService;
    private final StorageApiService _apiService;

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

    public StorageWSActionProcessor connect() {
        return _WSService.connect();
    }

    @Nullable
    public StorageWSActionProcessor getProcessorOrNull() {
        return this._WSService.getProcessor();
    }

    public StorageWSActionProcessor getProcessor() {
        StorageWSActionProcessor actionProcessor = this.getProcessorOrNull();
        if (actionProcessor == null) throw new IllegalStateException("Unable to set lamp state: not connected");
        return actionProcessor;
    }

    public HTTPApiRequest<String> writeRequest(@NonNull StorageRequestPayload payload) {
        return this._apiService.writeRequest(payload);
    }

    public HTTPApiRequest<Void> writeResponse(@NonNull StorageResponsePayload payload) {
        return this._apiService.writeResponse(payload);
    }
}
