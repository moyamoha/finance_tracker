package com.finance_tracker.dto.responses;

import com.finance_tracker.enums.TransactionCategory;
import com.finance_tracker.enums.TransactionType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
public class SingleTransactionResponse {

    private UUID id;
    private UUID userId;
    private TransactionType type;
    private TransactionCategory category;
    private BigDecimal amount;
    private Date date;
    private Date createdAt;

}
