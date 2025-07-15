package com.finance_tracker.exception.custom.recurring_transaction;

import com.finance_tracker.exception.http.BadRequestException;

public class InvalidDateRangeException extends BadRequestException {

    private static final String INVALID_RANGE = "The date range provided is invalid";
    private static final String END_DATE_BEFORE_START_DATE = "The end date cannot not before the start date";
    private static final String RANGE_TOO_SHORT = "The date range is too short";

    public InvalidDateRangeException() {
        super(INVALID_RANGE);
    }

    public InvalidDateRangeException(String message) {
        super(message);
    }

    public static InvalidDateRangeException withEndDateBeforeStartDate() {
        return new InvalidDateRangeException(END_DATE_BEFORE_START_DATE);
    }

    public static InvalidDateRangeException withRangeTooShort() {
        return new InvalidDateRangeException(RANGE_TOO_SHORT);
    }

}
