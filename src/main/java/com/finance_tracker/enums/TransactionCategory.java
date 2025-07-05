package com.finance_tracker.enums;

import lombok.Getter;

public enum TransactionCategory {
    FOOD("Food", TransactionType.EXPENSE),
    ENTERTAINMENT("Entertainment", TransactionType.EXPENSE),
    GROCERIES("Groceries", TransactionType.EXPENSE),
    RESTAURANTS("Restaurants", TransactionType.EXPENSE),
    TRANSPORTATION("Transportation", TransactionType.EXPENSE),
    FUEL("Fuel", TransactionType.EXPENSE),
    UTILITIES("Utilities", TransactionType.EXPENSE),
    RENT("Rent", TransactionType.EXPENSE),
    MORTGAGE("Mortgage", TransactionType.EXPENSE),
    INSURANCE("Insurance", TransactionType.EXPENSE),
    HEALTHCARE("Healthcare", TransactionType.EXPENSE),
    MEDICAL("Medical", TransactionType.EXPENSE),
    PERSONAL_CARE("Personal care", TransactionType.EXPENSE),
    EDUCATION("Education", TransactionType.EXPENSE),
    GIFTS("Gifts", TransactionType.EXPENSE),
    CHARITY("Charity", TransactionType.EXPENSE),
    TRAVEL("Travel", TransactionType.EXPENSE),
    SUBSCRIPTIONS("Subscriptions", TransactionType.EXPENSE),
    INVESTMENTS("Investments", TransactionType.EXPENSE),
    SAVINGS("Savings", TransactionType.EXPENSE),
    SALARY("Salary", TransactionType.INCOME),
    BONUS("Bonus", TransactionType.INCOME),
    TAXES("Taxes", TransactionType.EXPENSE),
    LOANS("Loans", TransactionType.INCOME),
    DEBT_PAYMENT("Debt payment", TransactionType.EXPENSE),
    DEPOSIT("Deposit", TransactionType.INCOME),
    WITHDRAWAL("Withdrawal", TransactionType.EXPENSE),
    OTHER("Other", TransactionType.EXPENSE);

    @Getter
    private final String displayName;

    @Getter
    private final TransactionType transactionType;

    TransactionCategory(String displayName, TransactionType transactionType) {
        this.displayName = displayName;
        this.transactionType = transactionType;
    }

}
