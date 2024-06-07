package com.smartplant.smartplantandroid.core.network.http.http_api_request;

import androidx.annotation.NonNull;

import okhttp3.Call;

public interface HTTPApiFailureCallback {
    void onFailure(@NonNull Call call, @NonNull Throwable error) throws Throwable;
}
