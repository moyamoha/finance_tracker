package com.finance_tracker.exception.custom.account;

import com.finance_tracker._shared.HttpErrorStatus;
import com.finance_tracker.exception.custom.CustomApplicationException;
import com.finance_tracker.exception.http.HttpException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class InsufficientFundsException extends HttpException {

    private final static String MESSAGE = "Operation failed due to insufficient funds for the account";

    public InsufficientFundsException() {
        super(HttpErrorStatus.UNPROCESSABLE_ENTITY, MESSAGE);
    }

    public InsufficientFundsException(String message) {
        super(HttpErrorStatus.UNPROCESSABLE_ENTITY, message);
    }

    public static InsufficientFundsException withAccountId(UUID accountId) {
        String message = MESSAGE + " \"" + accountId.toString() + "\"";
        return new InsufficientFundsException(message);
    }
}
