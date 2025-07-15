package com.finance_tracker._shared;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum HttpErrorStatus {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Bad Request"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Unauthorized"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "Forbidden"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "Not Found"),
    CONFLICT(HttpStatus.CONFLICT, "Conflict"),
    UNPROCESSABLE_ENTITY(HttpStatus.UNPROCESSABLE_ENTITY, "Unprocessable Entity"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error");

    @Getter
    private final HttpStatus statusCode;
    @Getter
    private final String statusText;

    HttpErrorStatus(HttpStatus statusCode, String statusText) {
        this.statusCode = statusCode;
        this.statusText = statusText;
    }

}
