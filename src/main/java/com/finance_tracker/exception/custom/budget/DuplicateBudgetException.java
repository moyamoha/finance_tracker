package com.finance_tracker.exception.custom.budget;

import com.finance_tracker._shared.HttpErrorStatus;
import com.finance_tracker.exception.http.HttpException;

public class DuplicateBudgetException extends  HttpException {

    private final static String MESSAGE = "Similar budget already exists";

    public DuplicateBudgetException() {
        super(HttpErrorStatus.CONFLICT, MESSAGE);
    }
}
