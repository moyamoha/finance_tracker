package com.finance_tracker.exception.authentication;

public class PasswordNotMatchedException extends CustomAuthenticationException {
    public PasswordNotMatchedException(String detail) {
        super(detail);
    }
}
