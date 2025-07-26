package com.finance_tracker.repository.transaction;

import com.finance_tracker.entity.Budget;
import com.finance_tracker.entity.Transaction;
import com.finance_tracker.entity.User;
import com.finance_tracker.enums.TransactionType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
                        (user_id = :userId) AND
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

    @Override
    @SuppressWarnings("unchecked")
    public List<Transaction> findExpensesByUserAndGivenMonth(User user, LocalDate date) {
        String rawSql = """
                    SELECT * FROM transaction
                    WHERE
                        user_id = :userId AND
                        date >= :firstDayOfCurrentMonth AND
                        ( date >= :firstDayOfCurrentMonth AND date < :firstDayOfNextMonth)
                """;
        int currentYear = date.getYear();
        int currMonth = date.getMonthValue();
        LocalDateTime firstDay = LocalDateTime.of(currentYear, currMonth, 1, 0, 0, 0);
        LocalDateTime nextMonthFirstDay = firstDay.plusMonths(1);

        return (List<Transaction>) entityManager.createNativeQuery(rawSql, Transaction.class)
                .setParameter("userId", user.getId())
                .setParameter("firstDayOfCurrentMonth", firstDay)
                .setParameter("firstDayOfNextMonth", nextMonthFirstDay)
                .getResultList();

    }

    @Override
    public BigDecimal totalByTransactionTypeForGivenMonth(User user, TransactionType type, LocalDate date) {
        String rawSql = """
                SELECT COALESCE(SUM(t.amount), 0) FROM transaction t
                WHERE
                    t.user_id = :userId AND
                    t.type = :type AND
                    (t.date >= :firstDayOfCurrentMonth AND t.date < :firstDayOfNextMonth)
            """;

        LocalDate firstDayOfCurrentMonth = date.withDayOfMonth(1);
        LocalDate firstDayOfNextMonth = firstDayOfCurrentMonth.plusMonths(1);

        Object resultObject = entityManager.createNativeQuery(rawSql)
                .setParameter("userId", user.getId())
                .setParameter("type", type.name())
                .setParameter("firstDayOfCurrentMonth", firstDayOfCurrentMonth)
                .setParameter("firstDayOfNextMonth", firstDayOfNextMonth)
                .getSingleResult();

        BigDecimal result = (BigDecimal) resultObject;

        return result.setScale(2, RoundingMode.HALF_UP);
    }
}
