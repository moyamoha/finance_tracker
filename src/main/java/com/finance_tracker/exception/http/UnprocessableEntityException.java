package com.finance_tracker.exception.http;

import com.finance_tracker._shared.HttpErrorStatus;

public class UnprocessableEntityException extends HttpException{
    public UnprocessableEntityException(Object detail) {
        super(HttpErrorStatus.UNPROCESSABLE_ENTITY, detail);
    }
}
