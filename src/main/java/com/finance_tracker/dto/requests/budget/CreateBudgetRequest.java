package com.finance_tracker.dto.requests.budget;

import com.finance_tracker.enums.BudgetPeriod;
import com.finance_tracker.enums.TransactionCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CreateBudgetRequest {

    @NotBlank
    private String name;

    private TransactionCategory category;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate startDate;

    @NotNull
    private BudgetPeriod period;

    private UUID accountId;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endDate;

    @NotNull
    @Positive
    private BigDecimal amount;
}
