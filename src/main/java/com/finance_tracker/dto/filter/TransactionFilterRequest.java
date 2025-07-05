package com.finance_tracker.dto.filter;

import com.finance_tracker.enums.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TransactionFilterRequest {

    private String type;

    @NotBlank
    @PositiveOrZero
    private BigDecimal minAmount;

    @NotBlank
    @PositiveOrZero
    private BigDecimal maxAmount;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date startDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date endDate;
}

