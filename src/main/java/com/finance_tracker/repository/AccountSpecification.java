package com.finance_tracker.repository;

import com.finance_tracker.entity.Account;
import com.finance_tracker.entity.Transaction;
import com.finance_tracker.enums.AccountType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public class AccountSpecification  {

    public static Specification<Account> hasUserId(UUID userId) {
        return ((root, query, cb) -> cb.equal(root.get("user").get("id"), userId));
    }

    public static Specification<Account> hasType(AccountType type) {
        return (root, query, cb) -> cb.equal(root.get("type"), type);
    }

    public static Specification<Account> nameContainsIgnoreCase(String name) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }
}
