package com.smartplant.smartplantandroid.storage.network.actions.request;

import androidx.annotation.NonNull;

public interface ActionRequestFailureHandler {
    void onFailure(@NonNull Throwable error);
}
