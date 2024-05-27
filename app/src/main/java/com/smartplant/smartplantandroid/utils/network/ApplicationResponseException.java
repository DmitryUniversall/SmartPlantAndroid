package com.smartplant.smartplantandroid.utils.network;


public class ApplicationResponseException extends Exception {
    private final ApplicationResponse _applicationResponse;

    public ApplicationResponseException(ApplicationResponse applicationResponse) {
        this._applicationResponse = applicationResponse;
    }

    public ApplicationResponse getApplicationResponse() {
        return _applicationResponse;
    }
}
