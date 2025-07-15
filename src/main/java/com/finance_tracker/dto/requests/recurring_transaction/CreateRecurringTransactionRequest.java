package com.finance_tracker.dto.requests.recurring_transaction;

import com.finance_tracker.enums.RecurrenceFrequency;
import com.finance_tracker.enums.TransactionCategory;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Data
@Builder
public class CreateRecurringTransactionRequest {

    @NotNull
    private UUID accountId;

    @NotNull
    private TransactionCategory category;

    private String description;

    @NotNull
    @Positive
    private BigDecimal amount;

    @NotNull
    @FutureOrPresent(message = "Recurring transaction cannot start in the past")
    private LocalDate startDate;

    @FutureOrPresent(message = "Recurring transaction cannot end in the past")
    private LocalDate endDate;

    @NotNull
    private RecurrenceFrequency frequency;
}
