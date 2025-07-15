package com.finance_tracker.exception.authentication;

public class InvalidEmailOrPasswordException extends CustomAuthenticationException {
    public InvalidEmailOrPasswordException(String detail) {
        super(detail);
    }
}
