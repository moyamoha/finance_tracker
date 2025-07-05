package com.finance_tracker.dto.requests.transaction;

import com.finance_tracker.enums.TransactionCategory;
import com.finance_tracker.enums.TransactionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
public class CreateTransactionRequest {

    @NotNull
    private UUID accountId;

    @NotNull
    private TransactionType type;

    @NotNull
    private TransactionCategory category;

    private String description;

    @NotNull
    @PositiveOrZero
    private BigDecimal amount;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date date;
}
