package com.finance_tracker.exception.authentication;

public class UserNotFoundException extends CustomAuthenticationException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
