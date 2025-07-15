package com.finance_tracker._shared;

import java.time.LocalDate;

public record LocalDateRange(LocalDate start, LocalDate end) {
    public boolean includes(LocalDate givenDate) {
        if (end == null) return givenDate.isAfter(start) || givenDate.isEqual(start);
        if (givenDate.isEqual(start) || givenDate.isEqual(end)) return true;
        return givenDate.isAfter(start) && givenDate.isBefore(end);
    }

    public boolean endIsBeforeStart() {
        return end != null && end.isBefore(start);
    }
}
