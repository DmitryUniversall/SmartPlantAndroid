package com.smartplant.smartplantandroid.utils.network.http.api_request;

import androidx.annotation.NonNull;

import com.smartplant.smartplantandroid.utils.network.TransferResponse;

import okhttp3.Response;

public interface ApiHttpSuccessHandler<T> {
    void onSuccess(@NonNull T result, @NonNull Response response, @NonNull TransferResponse transferResponse);
}
