package com.finance_tracker.dto.filter;

import com.finance_tracker.enums.TransactionCategory;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class BudgetFilterRequest {
    private String name;
    private UUID accountId;
    private TransactionCategory category;
    private Boolean isActive = false;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDateTime startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDateTime endDate;
}
