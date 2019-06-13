package com.amazon.exceptions;

public class InvalidProductException extends Exception {
    public InvalidProductException() {
    }

    public InvalidProductException(String message) {
        super(message);
    }

    public InvalidProductException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidProductException(Throwable cause) {
        super(cause);
    }

    public InvalidProductException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
