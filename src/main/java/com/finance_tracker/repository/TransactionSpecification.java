package com.finance_tracker.repository;

import com.finance_tracker.entity.Transaction;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public class TransactionSpecification {

    public static Specification<Transaction> hasUserId(UUID userId) {
        return ((root, query, cb) -> cb.equal(root.get("user").get("id"), userId));
    }

    public static Specification<Transaction> hasType(String type) {
        return (root, query, cb) -> cb.equal(root.get("type"), type);
    }

    public static Specification<Transaction> amountGreaterThanOrEqual(BigDecimal amount) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("amount"), amount);
    }

    public static Specification<Transaction> amountLessThanOrEqual(BigDecimal amount) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("amount"), amount);
    }

    public static Specification<Transaction> dateAfter(Date date) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("date"), date);
    }

    public static Specification<Transaction> dateBefore(Date date) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("date"), date);
    }
}
