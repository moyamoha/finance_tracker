package com.finance_tracker.dto.requests.account;

import com.finance_tracker.enums.AccountType;
import lombok.Data;

@Data
public class EditAccountRequest {
    private String name;
    private AccountType type;
}
