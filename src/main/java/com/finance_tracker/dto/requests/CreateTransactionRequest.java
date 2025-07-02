package com.finance_tracker.dto.requests;

import com.finance_tracker.enums.TransactionCategory;
import com.finance_tracker.enums.TransactionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class CreateTransactionRequest {

    @NotNull
    private TransactionType type;

    @NotNull
    private TransactionCategory category;

    private String description;

    @PositiveOrZero
    private BigDecimal amount;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date date;

}
