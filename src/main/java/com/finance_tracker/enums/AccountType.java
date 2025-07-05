package com.finance_tracker.enums;

import lombok.Getter;

public enum AccountType {
    BANK("Bank"),
    CASH("Cash"),
    CREDIT("Credit"),
    INVESTMENT("Investment"),
    SYSTEM("System"),
    OTHER("Other");

    @Getter
    private final String displayName;

    AccountType(String displayName) {
        this.displayName = displayName;
    }
}
