package com.smartplant.smartplantandroid.core.network.http.http_api_request;

import androidx.annotation.NonNull;

import com.smartplant.smartplantandroid.core.models.ApplicationResponse;

import okhttp3.Response;

public interface HTTPApiResponseProcessor<T> {
    T processResponse(@NonNull Response response, @NonNull ApplicationResponse applicationResponse);
}
