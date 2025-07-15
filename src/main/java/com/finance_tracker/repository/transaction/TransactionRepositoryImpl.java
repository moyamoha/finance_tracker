package com.finance_tracker.repository.transaction;

import com.finance_tracker.entity.Budget;
import com.finance_tracker.entity.Transaction;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

public class TransactionRepositoryImpl implements  TransactionRepositoryCustom{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @SuppressWarnings("unchecked")
    public List<Transaction> findForBudget(Budget budget) {
        String rawSql = """
                    SELECT * FROM transaction
                    WHERE
                        (user_id = :userId)
                        (account_id IS NULL OR account_id = :accountId) AND
                        category = :category AND
                        (date BETWEEN :start AND :end)
                """;
        return (List<Transaction>) entityManager.createNativeQuery(rawSql, Transaction.class)
                .setParameter("userId", budget.getUser().getId())
                .setParameter("accountId", budget.getAccount().getId())
                .setParameter("category", budget.getCategory().name())
                .setParameter("start", budget.getStartDate())
                .setParameter("end", budget.getEndDate())
                .getResultList();
    }
}
