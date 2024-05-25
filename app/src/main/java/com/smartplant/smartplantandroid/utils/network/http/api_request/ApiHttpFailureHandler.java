package com.smartplant.smartplantandroid.utils.network.http.api_request;

import androidx.annotation.NonNull;

import okhttp3.Call;

public interface ApiHttpFailureHandler {
    void onFailure(@NonNull Call call, @NonNull Throwable error) throws Throwable;
}
