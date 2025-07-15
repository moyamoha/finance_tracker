package com.finance_tracker.exception.authentication;

public class OtpCodeIncorrectOrExpiredException extends CustomAuthenticationException {
    public OtpCodeIncorrectOrExpiredException(String otpCode) {
        super(String.format("OTP code %s you provided is not correct or expired", otpCode));
    }
}
