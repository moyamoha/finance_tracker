package com.finance_tracker.dto.requests.transaction;

import com.finance_tracker.enums.TransactionCategory;
import com.finance_tracker.enums.TransactionType;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class EditTransactionRequest {
    private TransactionType type;

    private TransactionCategory category;
    private String description;

    @PositiveOrZero
    private BigDecimal amount;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date date;
}
