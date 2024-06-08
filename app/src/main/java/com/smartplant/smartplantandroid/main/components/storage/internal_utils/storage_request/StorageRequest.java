package com.smartplant.smartplantandroid.main.components.storage.internal_utils.storage_request;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.ReturnThis;

import com.google.gson.JsonObject;
import com.smartplant.smartplantandroid.core.exceptions.ApplicationResponseException;
import com.smartplant.smartplantandroid.core.exceptions.TimeOutException;
import com.smartplant.smartplantandroid.core.logs.AppLogger;
import com.smartplant.smartplantandroid.core.models.ApplicationResponse;
import com.smartplant.smartplantandroid.main.components.storage.internal_utils.processors.StorageWSProcessor;
import com.smartplant.smartplantandroid.main.components.storage.models.StorageDataMessage;
import com.smartplant.smartplantandroid.main.components.storage.models.StorageRequestPayload;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.UUID;

public class StorageRequest<T> {
    // Disposable callbacks
    private final @NonNull Queue<StorageRequestSuccessCallback<T>> _successCallbacks = new LinkedList<>();
    private final @NonNull Queue<StorageRequestFailureCallback> _failureCallbacks = new LinkedList<>();

    // Handler names
    private final @NonNull String _SARHandlerName = String.format("StorageRequest_%s", this._messageId);
    private final @NonNull String _DARHandlerName = String.format("StorageRequest_%s", this._messageId);

    // Utils
    protected final Handler loopHandler = new Handler(Looper.getMainLooper());

    // Payload data
    protected final @NonNull String _messageId = UUID.randomUUID().toString();
    protected final @Nullable JsonObject _payloadData;
    protected final int _targetId;

    // State
    protected boolean _done = false;

    // Options
    protected final int _timeout;

    // Other
    protected final @NonNull StorageWSProcessor _WSProcessor;
    protected final @NonNull StorageResponseProcessor<T> _responseProcessor;

    public static class Builder<T> {
        // Payload data
        private final @NonNull String _messageId = UUID.randomUUID().toString();
        private JsonObject _payloadData;
        private int _targetId;

        // Options
        private int _timeout = 30;

        // Other
        private StorageWSProcessor _WSProcessor;
        private StorageResponseProcessor<T> _responseProcessor;

        public StorageRequest<T> build() {
            assert this._targetId != 0;
            assert this._WSProcessor != null;
            assert this._responseProcessor != null;

            return new StorageRequest<>(this._targetId, this._payloadData, this._timeout, this._WSProcessor, this._responseProcessor);
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
        public Builder<T> setProcessor(StorageWSProcessor processor) {
            this._WSProcessor = processor;
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
            @NonNull StorageWSProcessor WSProcessor,
            @NonNull StorageResponseProcessor<T> responseProcessor
    ) {
        this._responseProcessor = responseProcessor;
        this._targetId = targetId;
        this._payloadData = payloadData;
        this._WSProcessor = WSProcessor;
        this._timeout = _timeout;

        this._selfRegister();
    }

    private void _selfRegister() {
        this._WSProcessor.onSAR(_SARHandlerName, this::_onSAR);
        this._WSProcessor.onDAR(_DARHandlerName, this::_onDAR);
    }

    protected StorageRequestPayload _generatePayload() {
        return new StorageRequestPayload(
                StorageRequestPayload.RequestType.ENQUEUE_REQUEST.getValue(),
                this._targetId,
                this._messageId,
                this._payloadData
        );
    }

    private void _callSuccessCallbacks(@NonNull T result, @NonNull ApplicationResponse response, @NonNull StorageDataMessage dataMessage) {
        _successCallbacks.forEach((handler) -> handler.onSuccess(result, response, dataMessage));
        _successCallbacks.clear();
    }

    private void _callFailureCallbacks(@NonNull Throwable error) {
        if (this._failureCallbacks.isEmpty()) {
            AppLogger.error("Unprocessed storage ws connection error", error);
            return;
        }

        Throwable currentError = error;
        while (!this._failureCallbacks.isEmpty()) {
            StorageRequestFailureCallback handler = this._failureCallbacks.poll();
            assert handler != null;

            try {
                handler.onFailure(currentError);
            } catch (Throwable e) {
                // Error occurred in last handler, so it will not be processed and will be lost
                if (this._failureCallbacks.isEmpty())
                    AppLogger.error("Unprocessed request error", e);
                currentError = e;
            }
        }
    }

    private void _cancelAfterTimeout() {
        if (this._timeout == 0) return;
        this.loopHandler.postDelayed(() -> this._cancel(new TimeOutException("Request timeout exceeded")), this._timeout * 1000L);
    }

    private synchronized void _cancel(@NonNull Throwable error) {
        if (this._done) return;

        AppLogger.info("StorageRequest %s canceled (with exception %s)", this._messageId, error.getClass().getSimpleName());

        this._done = true;
        this._callFailureCallbacks(error);
    }

    private synchronized void _processEnqueued(@NonNull ApplicationResponse response) throws ApplicationResponseException {
        if (this._done) return;
        if (!response.isOk()) throw new ApplicationResponseException(response);

        AppLogger.info("ActionRequest %s enqueued", this._messageId);
    }

    private synchronized void _processResponded(@NonNull StorageDataMessage dataMessage, @NonNull ApplicationResponse response) throws ApplicationResponseException {
        if (this._done) return;

        if (!response.isOk()) throw new ApplicationResponseException(response);

        AppLogger.debug("ActionRequest %s responded with success", _messageId);
        T result = _responseProcessor.processResponse(response, dataMessage);
        this._callSuccessCallbacks(result, response, dataMessage);
    }

    private void _onSAR(@NonNull ApplicationResponse response) {
        try {
            _processEnqueued(response);
        } catch (ApplicationResponseException error) {
            this._done = true;
            _callFailureCallbacks(error);
        } finally {  // FIXME: Concurrent modification as it deletes handler during forEach
            this._WSProcessor.removeSARHandler(_SARHandlerName);
        }
    }

    private void _onDAR(@NonNull StorageDataMessage dataMessage, @NonNull ApplicationResponse response) {
        if (!Objects.equals(dataMessage.getMessageId(), this._messageId)) return;

        try {
            _processResponded(dataMessage, response);
        } catch (ApplicationResponseException error) {
            _callFailureCallbacks(error);
        } finally {
            this._done = true;
            this._WSProcessor.removeDARHandler(_DARHandlerName);
        }
    }

    @ReturnThis
    public StorageRequest<T> onSuccess(StorageRequestSuccessCallback<T> callback) {
        this._successCallbacks.add(callback);
        return this;
    }

    @ReturnThis
    public StorageRequest<T> onFailure(StorageRequestFailureCallback callback) {
        this._failureCallbacks.add(callback);
        return this;
    }

    public void send() {
        StorageRequestPayload payload = this._generatePayload();
        this._WSProcessor.sendStorageRequestPayload(payload);
        this._cancelAfterTimeout();
    }
}
