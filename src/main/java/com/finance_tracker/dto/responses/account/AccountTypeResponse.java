package com.finance_tracker.dto.responses.account;

import com.finance_tracker.enums.AccountType;
import com.finance_tracker.enums.TransactionCategory;

public record AccountTypeResponse(String value, String displayName) {
    public static AccountTypeResponse fromAccountType(AccountType type) {
        return new AccountTypeResponse(type.name(), type.getDisplayName());
    }
}
