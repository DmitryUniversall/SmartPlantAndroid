package com.smartplant.smartplantandroid.main.components.auth.exceptions;

import androidx.annotation.Nullable;

import com.smartplant.smartplantandroid.core.exceptions.http.HttpApplicationResponseException;
import com.smartplant.smartplantandroid.core.models.ApplicationResponse;

public class AuthFailedException extends Exception {
    private @Nullable ApplicationResponse _applicationResponse;

    public AuthFailedException(String message) {
        super(message);
    }

    public AuthFailedException(Throwable cause) {
        super(cause);
    }

    public AuthFailedException(String message, HttpApplicationResponseException cause) {
        super(message, cause);
        this._applicationResponse = cause.getApplicationResponse();
    }

    @Nullable
    public ApplicationResponse getApplicationResponse() {
        return _applicationResponse;
    }
}
