package com.finance_tracker.enums;

import lombok.Getter;

public enum TransactionCategory {
    FOOD("Food"),
    ENTERTAINMENT("Entertainment"),
    GROCERIES("Groceries"),
    RESTAURANTS("Restaurants"),
    TRANSPORTATION("Transportation"),
    FUEL("Fuel"),
    UTILITIES("Utilities"),
    RENT("Rent"),
    MORTGAGE("Mortage"),
    INSURANCE("Insurance"),
    HEALTHCARE("Healthcare"),
    MEDICAL("Medical"),
    PERSONAL_CARE("Personal care"),
    EDUCATION("Education"),
    GIFTS("Gifts"),
    CHARITY("Charity"),
    TRAVEL("Travel"),
    SUBSCRIPTIONS("Subscriptions"),
    INVESTMENTS("Investments"),
    SAVINGS("Savings"),
    SALARY("Salary"),
    BONUS("Bonus"),
    TAXES("Taxes"),
    LOANS("Loans"),
    DEBT_PAYMENT("Debt payment"),
    OTHER("Other");

    @Getter
    private final String displayName;

    TransactionCategory(String displayName) {
        this.displayName = displayName;
    }

}
