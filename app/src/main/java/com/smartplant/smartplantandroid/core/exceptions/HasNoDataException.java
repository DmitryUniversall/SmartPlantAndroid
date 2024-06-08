package com.smartplant.smartplantandroid.core.exceptions;

public class HasNoDataException extends RuntimeException {
    public HasNoDataException() {
        super();
    }

    public HasNoDataException(String message) {
        super(message);
    }

    public HasNoDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public HasNoDataException(Throwable cause) {
        super(cause);
    }
}
