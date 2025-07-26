package com.finance_tracker.dto.responses.report;

import com.finance_tracker.enums.TransactionCategory;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ExpenseByCategoryItem {
    private TransactionCategory category;
    private BigDecimal value;
}
