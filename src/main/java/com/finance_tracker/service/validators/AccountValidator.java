package com.finance_tracker.service.validators;

import com.finance_tracker.entity.Account;
import com.finance_tracker.exception.custom.account.InsufficientFundsException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AccountValidator implements EntityValidator<Account> {
    @Override
    public void validate(Account account) {
        validateSufficientFundsInAccount(account);
    }

    public void validateSufficientFundsInAccount(Account account) {
        if (account.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw InsufficientFundsException.withAccountId(account.getId());
        }
    }
}
