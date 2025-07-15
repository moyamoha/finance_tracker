package com.finance_tracker.exception.http;

import com.finance_tracker._shared.HttpErrorStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class BadRequestException extends HttpException{
    public BadRequestException(Object detail) {
        super(HttpErrorStatus.BAD_REQUEST, detail);
    }
}
