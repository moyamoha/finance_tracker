package com.finance_tracker.enums;

import lombok.Getter;

public enum BudgetPeriod {
    WEEKLY("Weekly"),
    BIWEEKLY("Bi-weekly"),
    MONTHLY("Monthly"),
    QUARTERLY("Quarterly"),
    YEARLY("Yearly"),
    FIXED_LENGTH("Fixed length");

    @Getter
    private final String displayName;

    BudgetPeriod(String displayName) {
        this.displayName = displayName;
    }

    public boolean isFixedLength() {
        return this.name().equals(BudgetPeriod.FIXED_LENGTH.name());
    }

    public boolean isPeriodic() {
        return !this.name().equals(BudgetPeriod.FIXED_LENGTH.name());
    }
}
