package com.smartplant.smartplantandroid.utils.network.http.api_request;

import androidx.annotation.NonNull;

public interface ApiHttpSuccessHandler<T> {
    void onSuccess(@NonNull T result);
}
