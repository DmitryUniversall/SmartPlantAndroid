package com.smartplant.smartplantandroid.utils.network.http.excpetions;

import com.smartplant.smartplantandroid.utils.network.ApplicationResponse;

import okhttp3.Response;

public class HttpApplicationResponseException extends Exception {
    private final Response _response;
    private final ApplicationResponse _applicationResponse;

    public HttpApplicationResponseException(Response response, ApplicationResponse applicationResponse) {
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
