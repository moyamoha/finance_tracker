package com.finance_tracker.exception.custom.user;

import com.finance_tracker._shared.HttpErrorStatus;
import com.finance_tracker.exception.http.HttpException;

public class IncorrectPasswordForActionException extends HttpException {

    private static final String MESSAGE = "The provided password is incorrect. Please re-enter your current password to proceed.";

    public IncorrectPasswordForActionException() {
        super(HttpErrorStatus.FORBIDDEN, MESSAGE);
    }
}
