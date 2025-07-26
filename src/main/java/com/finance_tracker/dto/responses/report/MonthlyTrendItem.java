package com.finance_tracker.dto.responses.report;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class MonthlyTrendItem {
    private String month;
    private BigDecimal income;
    private BigDecimal expenses;
    private BigDecimal savings;
}
