package com.smartplant.smartplantandroid.core.exceptions;


import com.smartplant.smartplantandroid.core.models.ApplicationResponse;

public class ApplicationResponseException extends Exception {
    private final ApplicationResponse _applicationResponse;

    public ApplicationResponseException(ApplicationResponse applicationResponse) {
        this._applicationResponse = applicationResponse;
    }

    public ApplicationResponse getApplicationResponse() {
        return _applicationResponse;
    }
}
