package com.finance_tracker.exception.custom.user;

import com.finance_tracker.exception.http.BadRequestException;

public class MfaAlreadyEnabledException extends BadRequestException {
    public MfaAlreadyEnabledException(String email) {
        super(
                String.format("User '%s' has already enabled Multi-factor authentication", email)
        );
    }
}
