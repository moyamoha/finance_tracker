package com.finance_tracker.exception.custom.user;

import com.finance_tracker._shared.HttpErrorStatus;
import com.finance_tracker.exception.http.HttpException;

public class UserRegistrationException extends HttpException {
    public UserRegistrationException() {
        super(HttpErrorStatus.CONFLICT, "Registration failed");
    }
}
