package com.finance_tracker.exception.custom.user;

import com.finance_tracker._shared.HttpErrorStatus;
import com.finance_tracker.exception.http.HttpException;

public class UserAlreadyRegisteredException extends HttpException {
    public UserAlreadyRegisteredException(String email) {
        super(HttpErrorStatus.CONFLICT, "A user with email: " + email + " is already registered");
    }
}
