package com.finance_tracker.repository.transaction;

import com.finance_tracker.entity.Budget;
import com.finance_tracker.entity.Transaction;
import com.finance_tracker.entity.User;
import com.finance_tracker.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepositoryCustom {
    List<Transaction> findForBudget(Budget budget);
    List<Transaction> findExpensesByUserAndGivenMonth(User user, LocalDate date);
    BigDecimal totalByTransactionTypeForGivenMonth(User user, TransactionType type, LocalDate date);
}
