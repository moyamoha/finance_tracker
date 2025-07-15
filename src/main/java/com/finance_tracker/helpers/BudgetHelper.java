package com.finance_tracker.helpers;

import com.finance_tracker._shared.LocalDateRange;
import com.finance_tracker.entity.Budget;
import com.finance_tracker.enums.TransactionType;
import com.finance_tracker.exception.custom.budget.InvalidCategoryForBudgetException;
import com.finance_tracker.exception.custom.recurring_transaction.InvalidDateRangeException;

import java.time.LocalDate;

public class BudgetHelper {

    public static void validateBudgetCategoryOrThrow(Budget budget) {
        if (budget.getCategory().getTransactionType().equals(TransactionType.INCOME)) {
            throw new InvalidCategoryForBudgetException();
        }
    }

    public static void validateBudgetDateRange(Budget budget) {
        LocalDate start = budget.getStartDate();
        LocalDate end = budget.getEndDate();

        LocalDateRange range = new LocalDateRange(start, end);

        if (range.endIsBeforeStart()) {
            throw InvalidDateRangeException.withEndDateBeforeStartDate();
        }
    }
}
