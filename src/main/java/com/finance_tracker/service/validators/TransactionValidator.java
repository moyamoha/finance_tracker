package com.finance_tracker.service.validators;

import com.finance_tracker.entity.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionValidator implements EntityValidator<Transaction> {

    @Override
    public void validate(Transaction entity) {

    }
}
