package com.finance_tracker.exception.custom.account;

import com.finance_tracker._shared.HttpErrorStatus;
import com.finance_tracker.exception.http.HttpException;

public class AccountWithTheSameNameAlreadyExists extends HttpException {

    private final static String MESSAGE = "Account with the given name already exists";

    public AccountWithTheSameNameAlreadyExists() {
        super(HttpErrorStatus.CONFLICT, MESSAGE);
    }
}
