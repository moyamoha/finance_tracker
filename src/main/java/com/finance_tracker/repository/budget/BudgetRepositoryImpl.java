package com.finance_tracker.repository.budget;

import com.finance_tracker.entity.Budget;
import com.finance_tracker.entity.Transaction;
import com.finance_tracker.enums.TransactionType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.*;

@Repository
public class BudgetRepositoryImpl implements BudgetRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    public List<Budget> findForTransaction(Transaction transaction) {
        if (transaction.getType() != TransactionType.EXPENSE) {
            return Collections.emptyList();
        }
        String rawSql = """
                    SELECT * FROM budget
                    WHERE
                        user_id = :userId AND
                        (account_id IS NULL OR account_id = :accountId) AND
                        (category IS NULL OR category = :category) AND
                        is_active = 1 AND
                        :transactionDate BETWEEN start_date AND end_date
                """;
        return (List<Budget>) entityManager
                .createNativeQuery(rawSql, Budget.class)
                .setParameter("userId", transaction.getUser().getId())
                .setParameter("accountId", transaction.getAccount().getId())
                .setParameter("category", transaction.getCategory().name())
                .setParameter("transactionDate", transaction.getDate())
                .getResultList();
    }
}
