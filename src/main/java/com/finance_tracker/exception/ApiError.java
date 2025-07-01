package com.finance_tracker.exception;

import lombok.Getter;

import java.time.Instant;

@Getter
public class ApiError {
    private final Instant timestamp;
    private final int status;
    private final Object detail;
    private final String message;

    public ApiError(int status, Object detail, String message) {
        this.timestamp = Instant.now();
        this.detail = detail;
        this.status = status;
        this.message = message;
    }
}
