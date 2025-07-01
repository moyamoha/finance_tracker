package com.finance_tracker.enums;

import lombok.Getter;

public enum TransactionType {
    INCOME("income"),
    EXPENSE("expense");

    @Getter
    private final String displayName;

    TransactionType(String displayName) {
        this.displayName = displayName;
    }
}
