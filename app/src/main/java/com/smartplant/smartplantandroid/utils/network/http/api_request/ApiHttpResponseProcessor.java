package com.smartplant.smartplantandroid.utils.network.http.api_request;

import androidx.annotation.NonNull;

import com.smartplant.smartplantandroid.utils.network.TransferResponse;

import okhttp3.Response;

public interface ApiHttpResponseProcessor<T> {
    @NonNull
    T processResponse(@NonNull Response response, @NonNull TransferResponse transferResponse);
}
