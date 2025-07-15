package com.finance_tracker.helpers;

import com.finance_tracker.entity.RecurringTransaction;

import java.time.LocalDate;

public class RecurringTransactionHelper {

    public static LocalDate calculateNextGenerationDate(RecurringTransaction rt) {
        LocalDate start = rt.getLastGeneratedDate() != null ? rt.getLastGeneratedDate() : rt.getStartDate();
        LocalDate result = null;
        switch (rt.getFrequency()) {
            case DAILY -> result = start.plusDays(1);
            case WEEKLY -> result = start.plusWeeks(1);
            case BI_WEEKLY -> result = start.plusWeeks(2);
            case MONTHLY -> result = start.plusMonths(1);
            case QUARTERLY -> result = start.plusMonths(3);
            case ANNUALLY -> result = start.plusYears(1);
        }
        return result;
    }
}
