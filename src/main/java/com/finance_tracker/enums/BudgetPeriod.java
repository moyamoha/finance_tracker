package com.finance_tracker.enums;

import lombok.Getter;

public enum BudgetPeriod {
    WEEKLY("Weekly"),
    MONTHLY("Monthly"),
    QUARTERLY("Quarterly"),
    YEARLY("Yearly");

    @Getter
    private final String displayName;

    BudgetPeriod(String displayName) {
        this.displayName = displayName;
    }

}
