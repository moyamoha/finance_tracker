package com.finance_tracker.exception.http;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class BadRequestException extends HttpException{
    public BadRequestException(Object detail) {
        super(HttpStatus.BAD_REQUEST, "Bad request", detail);
    }
}
