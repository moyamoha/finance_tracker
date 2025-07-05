package com.finance_tracker.exception.custom.account;

import com.finance_tracker.exception.custom.CustomApplicationException;

import java.util.UUID;

public class InsufficientFundsException extends CustomApplicationException {

    private final static String MESSAGE = "Operation failed due to insufficient funds for the account";
    public InsufficientFundsException() {
        super(MESSAGE);
    }

    public InsufficientFundsException(String message) {
        super(message);
    }

    public static InsufficientFundsException withAccountId(UUID accountId) {
        String message = MESSAGE + " \"" + accountId.toString() + "\"";
        return new InsufficientFundsException(message);
    }
}
