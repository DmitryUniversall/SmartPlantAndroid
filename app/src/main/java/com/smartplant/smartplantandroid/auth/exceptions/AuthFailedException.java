package com.smartplant.smartplantandroid.auth.exceptions;

import androidx.annotation.Nullable;

import com.smartplant.smartplantandroid.utils.network.TransferResponse;
import com.smartplant.smartplantandroid.utils.network.http.excpetions.HttpTransferResponseException;

public class AuthFailedException extends Exception {
    private @Nullable TransferResponse transferResponse;

    public AuthFailedException() {
        super();  // TODO: Default "auth failed" message
    }

    public AuthFailedException(String message) {
        super(message);
    }

    public AuthFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthFailedException(HttpTransferResponseException cause) {
        super(cause);
        this.transferResponse = cause.getTransferResponse();
    }

    public AuthFailedException(Throwable cause) {
        super(cause);
    }

    @Nullable
    public TransferResponse getTransferResponse() {
        return transferResponse;
    }
}
