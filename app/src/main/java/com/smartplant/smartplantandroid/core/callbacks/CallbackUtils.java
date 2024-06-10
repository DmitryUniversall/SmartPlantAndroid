package com.smartplant.smartplantandroid.core.callbacks;

import androidx.annotation.NonNull;

import com.smartplant.smartplantandroid.core.logs.AppLogger;

import java.util.Collection;
import java.util.Queue;

public class CallbackUtils {
    public static void callFailureCallbackQueue(@NonNull Throwable error, @NonNull Queue<FailureCallback> callbacks) {
        if (callbacks.isEmpty()) return;

        Throwable currentError = error;
        while (!callbacks.isEmpty()) {
            FailureCallback callback = callbacks.poll();
            assert callback != null;

            try {
                callback.onFailure(currentError);
            } catch (Throwable e) {
                // Error occurred in last callback, so it will not be processed and will be lost
                if (callbacks.isEmpty())
                    AppLogger.error("Unprocessed failure error (occurred in last callback)", e);
                currentError = e;
            }
        }
    }

    public static <T> void callSuccessCallbacks(@NonNull T result, @NonNull Collection<SuccessCallback<T>> callbacks) {
        if (callbacks.isEmpty()) return;
        callbacks.forEach((callback) -> callback.onSuccess(result));
    }

    public static void callRunnableCallbacks(@NonNull Collection<Runnable> callbacks) {
        if (callbacks.isEmpty()) return;
        callbacks.forEach(Runnable::run);
    }
}
