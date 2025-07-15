package com.finance_tracker.exception.custom.budget;

import com.finance_tracker.exception.http.UnprocessableEntityException;

public class InvalidCategoryForBudgetException extends UnprocessableEntityException {

    private static final String MESSAGE = "Budget cannot be set on category type of INCOME";

    public InvalidCategoryForBudgetException() {
        super(MESSAGE);
    }
}
