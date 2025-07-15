package com.finance_tracker.repository.budget;

import com.finance_tracker._shared.LocalDateTimeRange;
import com.finance_tracker.entity.Budget;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;
import java.util.UUID;

public class BudgetSpecification {
    public static Specification<Budget> hasUserId(UUID userId) {
        return ((root, query, cb) -> cb.equal(root.get("user").get("id"), userId));
    }

    public static Specification<Budget> hasAccountId(UUID accountId) {
        return ((root, query, cb) -> cb.equal(root.get("account").get("id"), accountId));
    }

    public static Specification<Budget> hasCategory(String category) {
        return (root, query, cb) -> cb.equal(root.get("category"), category);
    }

    public static Specification<Budget> isInRange(LocalDateTimeRange range) {
        return (root, query, cb) -> {
            return cb.and(
                    cb.greaterThanOrEqualTo(root.get("start_date"), range.getStart()),
                    cb.lessThanOrEqualTo(root.get("end_date"), range.getEnd())
            );
        };
    }

    public static Specification<Budget> isActive() {
        return (root, query, cb) -> cb.equal(root.get("is_active"), true);
    }

    public static Specification<Budget> dateAfter(Date date) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("date"), date);
    }

    public static Specification<Budget> dateBefore(Date date) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("date"), date);
    }

    public static Specification<Budget> nameContainsIgnoreCase(String name) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }
}
