package com.smartplant.smartplantandroid.main.components.storage.utils.storage_request;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.ReturnThis;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.smartplant.smartplantandroid.core.async.ExecutedInBackground;
import com.smartplant.smartplantandroid.core.callbacks.CallbackUtils;
import com.smartplant.smartplantandroid.core.callbacks.FailureCallback;
import com.smartplant.smartplantandroid.core.data.json.JsonUtils;
import com.smartplant.smartplantandroid.core.exceptions.ApplicationResponseException;
import com.smartplant.smartplantandroid.core.exceptions.TimeOutException;
import com.smartplant.smartplantandroid.core.logs.AppLogger;
import com.smartplant.smartplantandroid.core.models.ApplicationResponse;
import com.smartplant.smartplantandroid.main.components.storage.internal_utils.processors.StorageWSProcessor;
import com.smartplant.smartplantandroid.main.components.storage.models.StorageDataMessage;
import com.smartplant.smartplantandroid.main.components.storage.models.write.StorageRequestPayload;
import com.smartplant.smartplantandroid.main.components.storage.repository.StorageRepositoryST;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.UUID;

public class StorageRequest<T> implements ExecutedInBackground<StorageRequestSuccessCallback<T>, FailureCallback> {
    // Utils
    protected static final Handler _loopHandler = new Handler(Looper.getMainLooper());
    protected static final Gson _gson = JsonUtils.getGson();

    // Disposable callbacks
    protected final @NonNull Queue<StorageRequestSuccessCallback<T>> _successCallbacks = new LinkedList<>();
    protected final @NonNull Queue<FailureCallback> _failureCallbacks = new LinkedList<>();
    protected final @NonNull Queue<Runnable> _afterCallbacks = new LinkedList<>();

    // Handler name
    protected final @NonNull String _DARHandlerName = String.format("StorageRequest_%s", this._id);

    // State
    protected boolean _done = false;

    // Options
    protected final int _timeout;

    // Payload data
    protected final int _targetId;
    protected final @Nullable JsonObject _data;

    protected final @NonNull StorageRepositoryST _storageRepository;
    protected final @NonNull StorageWSProcessor _WSProcessor;
    protected final @NonNull StorageResponseProcessor<T> _responseProcessor;
    protected final @NonNull String _id = UUID.randomUUID().toString();
    protected @Nullable String _requestUUID;

    public static class Builder<T> {
        // Payload data
        private JsonObject _payloadData;
        private int _targetId;

        // Options
        private int _timeout = 30;

        // Other
        private StorageResponseProcessor<T> _responseProcessor;

        public StorageRequest<T> build() {
            assert this._targetId != 0;
            assert this._responseProcessor != null;

            return new StorageRequest<>(this._targetId, this._payloadData, this._timeout, this._responseProcessor);
        }

        @ReturnThis
        public Builder<T> setPayloadData(@Nullable JsonObject _payloadData) {
            this._payloadData = _payloadData;
            return this;
        }

        public Builder<T> setTargetId(int targetId) {
            this._targetId = targetId;
            return this;
        }

        @ReturnThis
        public Builder<T> setResponseProcessor(StorageResponseProcessor<T> responseProcessor) {
            this._responseProcessor = responseProcessor;
            return this;
        }

        @ReturnThis
        public Builder<T> setTimeout(int timeout) {
            this._timeout = timeout;
            return this;
        }
    }

    protected StorageRequest(
            int targetId,
            @Nullable JsonObject payloadData,
            int _timeout,
            @NonNull StorageResponseProcessor<T> responseProcessor
    ) {
        this._storageRepository = StorageRepositoryST.getInstance();
        this._WSProcessor = this._storageRepository.getProcessor();
        this._responseProcessor = responseProcessor;
        this._targetId = targetId;
        this._data = payloadData;
        this._timeout = _timeout;

        this._selfRegister();
    }

    protected void _selfRegister() {
        this._WSProcessor.onDAR(_DARHandlerName, this::_onDAR);
    }

    protected synchronized void _callSuccessCallbacks(@NonNull T result, @NonNull StorageDataMessage dataMessage, @NonNull ApplicationResponse response) {
        if (this._done) return;
        this._done = true;

        _successCallbacks.forEach((handler) -> handler.onSuccess(result, dataMessage, response));
        _successCallbacks.clear();
        this._callAfterCallbacks();
    }

    protected synchronized void _callFailureCallbacks(@NonNull Throwable error) {
        if (this._done) return;
        this._done = true;

        if (this._failureCallbacks.isEmpty()) {
            AppLogger.error("Unprocessed storage request error", error);
            return;
        }

        CallbackUtils.callFailureCallbackQueue(error, this._failureCallbacks);
        this._callAfterCallbacks();
    }

    protected synchronized void _callAfterCallbacks() {
        CallbackUtils.callRunnableCallbacks(this._afterCallbacks);
        this._afterCallbacks.clear();
    }

    protected void cleanup() {
        this._WSProcessor.removeDARHandler(_DARHandlerName);
    }

    protected StorageRequestPayload _generatePayload() {
        return new StorageRequestPayload(
                this._targetId,
                this._data
        );
    }

    protected void _cancel(@NonNull Throwable error) {
        if (this._done) return;

        AppLogger.info("StorageRequest %s (RequestUUID %s) canceled with exception %s", this._id, this._requestUUID, error.getClass().getSimpleName());
        this._callFailureCallbacks(error);
        this.cleanup();
    }

    protected void _cancelAfterTimeout() {
        if (this._timeout == 0) return;
        _loopHandler.postDelayed(() -> this._cancel(new TimeOutException("Request timeout exceeded")), this._timeout * 1000L);
    }

    protected void _processEnqueued() {
        if (this._done) return;
        AppLogger.info("StorageRequest %s (RequestUUID %s) successfully enqueued", this._id, this._requestUUID);
    }

    protected void _processResponded(@NonNull StorageDataMessage dataMessage, @NonNull ApplicationResponse response) throws ApplicationResponseException {
        if (this._done) return;
        if (!response.isOk()) throw new ApplicationResponseException(response);

        AppLogger.info("StorageRequest %s (RequestUUID %s) responded with success", this._id, this._requestUUID);
        T result = _responseProcessor.processResponse(dataMessage, response);
        this._callSuccessCallbacks(result, dataMessage, response);
    }

    protected void _onDAR(@NonNull StorageDataMessage dataMessage, @NonNull ApplicationResponse response) {
        if (this._requestUUID == null) return;
        if (!Objects.equals(dataMessage.getRequestUUID(), this._requestUUID)) return;

        try {
            _processResponded(dataMessage, response);
        } catch (Exception error) {
            _callFailureCallbacks(error);
        } finally {
            this.cleanup();
        }
    }

    protected void _sendWriteRequest(@NonNull StorageRequestPayload payload) {
        this._storageRepository.writeRequest(payload)
                .onSuccess(((result, response, applicationResponse) -> {
                    this._requestUUID = result;
                    this._processEnqueued();
                }))
                .onFailure(this::_cancel)
                .send();
    }

    public void send() {
        StorageRequestPayload payload = this._generatePayload();
        this._sendWriteRequest(payload);
        this._cancelAfterTimeout();
    }

    @ReturnThis
    public StorageRequest<T> onSuccess(StorageRequestSuccessCallback<T> callback) {
        this._successCallbacks.add(callback);
        return this;
    }

    @ReturnThis
    public StorageRequest<T> onFailure(FailureCallback callback) {
        this._failureCallbacks.add(callback);
        return this;
    }

    @ReturnThis
    public StorageRequest<T> after(Runnable callback) {
        this._afterCallbacks.add(callback);
        return this;
    }
}
