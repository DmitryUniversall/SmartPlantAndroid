package com.smartplant.smartplantandroid.core.async.background_task;

import androidx.annotation.NonNull;
import androidx.annotation.ReturnThis;

import com.smartplant.smartplantandroid.core.async.ExecutedInBackground;
import com.smartplant.smartplantandroid.core.callbacks.CallbackUtils;
import com.smartplant.smartplantandroid.core.callbacks.FailureCallback;
import com.smartplant.smartplantandroid.core.callbacks.SuccessCallback;
import com.smartplant.smartplantandroid.core.logs.AppLogger;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class BackgroundTask<T> implements ExecutedInBackground<SuccessCallback<T>, FailureCallback> {
    // Utils
    protected static final Executor executor = Executors.newSingleThreadExecutor();

    // Disposable callbacks
    protected final @NonNull Queue<SuccessCallback<T>> _successCallbacks = new LinkedList<>();
    protected final @NonNull Queue<FailureCallback> _failureCallbacks = new LinkedList<>();
    protected final @NonNull Queue<Runnable> _afterCallbacks = new LinkedList<>();

    protected final Callable<T> _task;

    public BackgroundTask(Callable<T> task) {
        this._task = task;
    }

    protected synchronized void _callSuccessCallbacks(T result) {
        CallbackUtils.callSuccessCallbacks(result, _successCallbacks);
        this._successCallbacks.clear();
    }

    protected synchronized void _callFailureCallbacks(@NonNull Throwable error) {
        if (this._failureCallbacks.isEmpty()) {
            AppLogger.error("Unprocessed BackgroundTask error", error);
            return;
        }

        CallbackUtils.callFailureCallbackQueue(error, this._failureCallbacks);
    }

    protected synchronized void _callAfterCallbacks() {
        CallbackUtils.callRunnableCallbacks(this._afterCallbacks);
        this._afterCallbacks.clear();
    }

    protected void _executeTask() {
        try {
            T result = _task.call();
            this._callSuccessCallbacks(result);
        } catch (Exception error) {
            this._callFailureCallbacks(error);
        } finally {
            this._callAfterCallbacks();
        }
    }

    public void execute() {
        executor.execute(this::_executeTask);
    }

    @ReturnThis
    public BackgroundTask<T> onSuccess(SuccessCallback<T> callback) {
        this._successCallbacks.add(callback);
        return this;
    }

    @ReturnThis
    public BackgroundTask<T> onFailure(FailureCallback callback) {
        _failureCallbacks.add(callback);
        return this;
    }

    @ReturnThis
    public BackgroundTask<T> after(Runnable callback) {
        this._afterCallbacks.add(callback);
        return this;
    }
}
