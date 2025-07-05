package com.finance_tracker.enums;

import lombok.Getter;

public enum TransactionType {
    INCOME("Income"),
    EXPENSE("Expense");

    @Getter
    private final String displayName;

    TransactionType(String displayName) {
        this.displayName = displayName;
    }
}
