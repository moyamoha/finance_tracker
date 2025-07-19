package com.finance_tracker.service.validators;

import com.finance_tracker._shared.LocalDateRange;
import com.finance_tracker.entity.Budget;
import com.finance_tracker.enums.TransactionType;
import com.finance_tracker.exception.custom.budget.InvalidCategoryForBudgetException;
import com.finance_tracker.exception.custom.recurring_transaction.InvalidDateRangeException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class BudgetValidator implements EntityValidator<Budget> {

    @Getter
    @Setter
    private Budget budget;

    @Override
    public void validate(Budget budget) {
        setBudget(budget);
        validateBudgetCategoryOrThrow();
        validateBudgetDateRange();
    }

    private void validateBudgetCategoryOrThrow() {
        if (budget.getCategory().getTransactionType().equals(TransactionType.INCOME)) {
            throw new InvalidCategoryForBudgetException();
        }
    }

    private void validateBudgetDateRange() {
        LocalDate start = budget.getStartDate();
        LocalDate end = budget.getEndDate();

        boolean isFixedRanged = budget.getPeriod().isFixedLength();

        LocalDateRange range = new LocalDateRange(start, end);

        if (end == null && isFixedRanged) {
            throw new InvalidDateRangeException("A fixed ranged budget must have an end date");
        }

        if (range.endIsBeforeStart()) {
            throw InvalidDateRangeException.withEndDateBeforeStartDate();
        }
    }
}
