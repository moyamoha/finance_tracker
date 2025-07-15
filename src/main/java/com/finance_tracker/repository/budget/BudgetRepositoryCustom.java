package com.finance_tracker.repository.budget;

import com.finance_tracker.entity.Budget;
import com.finance_tracker.entity.Transaction;
import java.util.List;

public interface BudgetRepositoryCustom {
    List<Budget> findForTransaction(Transaction transaction);
}
