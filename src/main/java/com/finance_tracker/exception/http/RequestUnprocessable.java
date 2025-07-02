package com.finance_tracker.exception.http;

import org.springframework.http.HttpStatus;

public class RequestUnprocessable extends HttpException {

    private static final String MESSAGE = "Content unprocessable";

    public RequestUnprocessable(Object payload) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, MESSAGE, payload);
    }
}
