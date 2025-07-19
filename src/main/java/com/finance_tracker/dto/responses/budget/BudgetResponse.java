package com.finance_tracker.dto.responses.budget;

import com.finance_tracker.enums.BudgetPeriod;
import com.finance_tracker.enums.TransactionCategory;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class BudgetResponse {
    private UUID id;
    private UUID userId;
    private UUID accountId;
    private TransactionCategory category;
    private BudgetPeriod period;
    private String name;
    private BigDecimal amount;
    private BigDecimal remaining;
    private Boolean isActive;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
