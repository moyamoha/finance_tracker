package com.finance_tracker.repository.transaction;

import com.finance_tracker.entity.Budget;
import com.finance_tracker.entity.Transaction;

import java.util.List;

public interface TransactionRepositoryCustom {
    List<Transaction> findForBudget(Budget budget);
}
