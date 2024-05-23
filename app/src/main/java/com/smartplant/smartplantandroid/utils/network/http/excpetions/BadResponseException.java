package com.smartplant.smartplantandroid.utils.network.http.excpetions;

public class BadResponseException extends Exception {  // TODO: Constructor with okhttp.Response
    public BadResponseException() {
        super();
    }

    public BadResponseException(String message) {
        super(message);
    }

    public BadResponseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadResponseException(Throwable cause) {
        super(cause);
    }
}
