package com.finance_tracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;

public class InvalidOrExpiredToken extends HttpException {
    public InvalidOrExpiredToken(Object payload) {
        super(HttpStatus.UNAUTHORIZED, "Unauthorized", payload);
    }
}
