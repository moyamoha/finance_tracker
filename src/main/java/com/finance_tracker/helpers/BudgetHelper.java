package com.finance_tracker.helpers;

import com.finance_tracker._shared.LocalDateRange;
import com.finance_tracker.entity.Budget;
import com.finance_tracker.enums.BudgetPeriod;
import com.finance_tracker.enums.TransactionType;
import com.finance_tracker.exception.custom.budget.InvalidCategoryForBudgetException;
import com.finance_tracker.exception.custom.recurring_transaction.InvalidDateRangeException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;

public class BudgetHelper {

    public static boolean budgetThresholdReached(Budget budget) {
        BigDecimal amount = budget.getAmount();
        BigDecimal remaining = budget.getRemaining();
        BigDecimal hundred = new BigDecimal("100");
        BigDecimal eighty = new BigDecimal("80");
        BigDecimal spent = amount.subtract(remaining);
        BigDecimal percentage = spent.multiply(hundred)
                .divide(amount, 0, RoundingMode.HALF_UP);
        int compared = percentage.compareTo(eighty);
        return compared >= 0;
    }

    public static boolean shouldBudgetBeReset(Budget budget) {
        if (budget.getPeriod().isFixedLength()) {
            return false;
        }
        if (budget.getRemaining().equals(budget.getAmount())) return false;
        LocalDate today = LocalDate.now();
        int n = 1;
        LocalDate iter = budget.getStartDate();
        while (true) {
            iter = addToLocalDate(iter, budget.getPeriod(), n);
            if (iter.equals(today)) {
                return true;
            } else if (iter.isAfter(today)) {
                return false;
            } else {
                n += 1;
            }
        }
    }

    private static LocalDate addToLocalDate(LocalDate date, BudgetPeriod period, int num) {
        switch (period) {
            case WEEKLY -> {
                return date.plusWeeks(num);
            }
            case MONTHLY -> {
                return date.plusMonths(num);
            }
            case YEARLY -> {
                return date.plusYears(1);
            }
            case BIWEEKLY -> {
                return date.plusWeeks(num * 2L);
            }
            case QUARTERLY -> {
                return date.plusMonths(3L * num);
            }
        }
        return date;
    }
}
