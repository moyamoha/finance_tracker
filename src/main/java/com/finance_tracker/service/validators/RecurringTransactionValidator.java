package com.finance_tracker.service.validators;

import com.finance_tracker._shared.LocalDateRange;
import com.finance_tracker.entity.RecurringTransaction;
import com.finance_tracker.exception.custom.recurring_transaction.InvalidDateRangeException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class RecurringTransactionValidator implements EntityValidator<RecurringTransaction> {

    @Getter @Setter
    private RecurringTransaction rt;

    @Override
    public void validate(RecurringTransaction entity) {
        setRt(entity);
        validateRecurringTransactionDateRange();
    }

    private void validateRecurringTransactionDateRange() {
        LocalDate start = rt.getStartDate();
        LocalDate end = rt.getEndDate();

        LocalDateRange range = new LocalDateRange(start, end);

        if (range.endIsBeforeStart()) {
            throw InvalidDateRangeException.withEndDateBeforeStartDate();
        }

        if (!range.includes(rt.getNextGenerationDate())) {
            throw InvalidDateRangeException.withRangeTooShort();
        }
    }
}
