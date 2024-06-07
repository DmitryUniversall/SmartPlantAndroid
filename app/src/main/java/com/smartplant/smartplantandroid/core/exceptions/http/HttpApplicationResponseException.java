package com.smartplant.smartplantandroid.core.exceptions.http;

import com.smartplant.smartplantandroid.core.exceptions.ApplicationResponseException;
import com.smartplant.smartplantandroid.core.models.ApplicationResponse;

import okhttp3.Response;

public class HttpApplicationResponseException extends ApplicationResponseException {
    private final Response _response;
    private final ApplicationResponse _applicationResponse;

    public HttpApplicationResponseException(ApplicationResponse applicationResponse, Response response) {
        super(applicationResponse);

        this._response = response;
        this._applicationResponse = applicationResponse;
    }

    public Response getResponse() {
        return _response;
    }

    public ApplicationResponse getApplicationResponse() {
        return _applicationResponse;
    }
}
