package com.finance_tracker.dto.responses.account;

import com.finance_tracker.enums.AccountType;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
public class SingleAccountResponse {
    private UUID id;
    private UUID userId;
    private AccountType type;
    private String name;
    private BigDecimal balance;
    private Date createdAt;
    private Date updatedAt;
}
