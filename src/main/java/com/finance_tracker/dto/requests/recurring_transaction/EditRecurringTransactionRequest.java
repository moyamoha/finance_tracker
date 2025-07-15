package com.finance_tracker.dto.requests.recurring_transaction;

import com.finance_tracker.enums.RecurrenceFrequency;
import com.finance_tracker.enums.TransactionCategory;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class EditRecurringTransactionRequest {
    private TransactionCategory category;
    private String description;

    @Positive
    private BigDecimal amount;

    @FutureOrPresent(message = "Recurring transaction cannot start in the past")
    private LocalDate startDate;

    @FutureOrPresent(message = "Recurring transaction cannot end in the past")
    private LocalDate endDate;

    private RecurrenceFrequency frequency;
}
