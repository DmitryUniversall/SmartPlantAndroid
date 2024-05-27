package com.smartplant.smartplantandroid.auth.exceptions;

import androidx.annotation.Nullable;

import com.smartplant.smartplantandroid.utils.network.ApplicationResponse;
import com.smartplant.smartplantandroid.utils.network.http.excpetions.HttpApplicationResponseException;

public class AuthFailedException extends Exception {
    private @Nullable ApplicationResponse applicationResponse;

    public AuthFailedException() {
        super();  // TODO: Default "auth failed" message
    }

    public AuthFailedException(String message) {
        super(message);
    }

    public AuthFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthFailedException(HttpApplicationResponseException cause) {
        super(cause);
        this.applicationResponse = cause.getApplicationResponse();
    }

    public AuthFailedException(Throwable cause) {
        super(cause);
    }

    @Nullable
    public ApplicationResponse getTransferResponse() {
        return applicationResponse;
    }
}
