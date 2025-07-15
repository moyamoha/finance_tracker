package com.finance_tracker.exception.authentication;

public class InvalidOrExpiredToken extends CustomAuthenticationException {

    private static final String MESSAGE = "Invalid or expired token";
    public InvalidOrExpiredToken() {
        super(MESSAGE);
    }
}
