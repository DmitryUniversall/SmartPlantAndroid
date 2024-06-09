package com.smartplant.smartplantandroid.core.callbacks;

import androidx.annotation.NonNull;

public interface FailureCallback {
    void onFailure(@NonNull Throwable error) throws Throwable;
}
