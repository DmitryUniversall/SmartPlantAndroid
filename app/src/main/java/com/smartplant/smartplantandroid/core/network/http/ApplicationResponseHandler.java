package com.smartplant.smartplantandroid.core.network.http;

import com.smartplant.smartplantandroid.core.models.ApplicationResponse;

public interface ApplicationResponseHandler {
    void onApplicationResponse(ApplicationResponse response);
}
