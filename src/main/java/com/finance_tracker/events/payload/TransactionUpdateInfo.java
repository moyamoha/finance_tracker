package com.finance_tracker.events.payload;

import com.finance_tracker.enums.TransactionType;
import java.math.BigDecimal;

public record TransactionUpdateInfo(BigDecimal amount, TransactionType type) {
}