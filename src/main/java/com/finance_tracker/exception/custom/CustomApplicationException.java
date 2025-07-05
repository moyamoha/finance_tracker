package com.finance_tracker.exception.custom;

public class CustomApplicationException extends RuntimeException {
    public CustomApplicationException(String message) {
        super(message);
    }
}
