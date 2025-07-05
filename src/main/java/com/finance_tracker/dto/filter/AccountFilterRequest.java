package com.finance_tracker.dto.filter;

import com.finance_tracker.enums.AccountType;
import lombok.Data;

@Data
public class AccountFilterRequest {
    private String name;
    private AccountType type;
}
