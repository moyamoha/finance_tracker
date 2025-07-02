package com.finance_tracker.exception.authentication;

public class InvalidOrExpiredToken extends CustomAuthenticationException {

    public InvalidOrExpiredToken(String detailMessage) {
        super(detailMessage);
    }
}
