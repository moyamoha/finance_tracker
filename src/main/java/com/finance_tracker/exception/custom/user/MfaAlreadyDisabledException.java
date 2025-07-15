package com.finance_tracker.exception.custom.user;

import com.finance_tracker.exception.http.BadRequestException;

public class MfaAlreadyDisabledException extends BadRequestException {
    public MfaAlreadyDisabledException(String email) {
        super(
                String.format("User '%s' has already disabled Multi-factor authentication", email)
        );
    }
}
