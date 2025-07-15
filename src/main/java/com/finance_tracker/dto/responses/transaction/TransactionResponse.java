package com.finance_tracker.dto.responses.transaction;

import com.finance_tracker.enums.TransactionCategory;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TransactionResponse {
    private UUID id;
    private UUID userId;
    private UUID accountId;
    private TransactionCategory category;
    private String description;
    private BigDecimal amount;
    private LocalDateTime date;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
