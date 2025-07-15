package com.finance_tracker.dto.responses.recurring_transaction;

import com.finance_tracker.enums.RecurrenceFrequency;
import com.finance_tracker.enums.TransactionCategory;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class RecurringTransactionResponse {
    private UUID id;
    private UUID userId;
    private UUID accountId;
    private TransactionCategory category;
    private RecurrenceFrequency frequency;
    private String description;
    private BigDecimal amount;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate lastGeneratedDate;
    private LocalDate nextGenerationDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
