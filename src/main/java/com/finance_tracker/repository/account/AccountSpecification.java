package com.finance_tracker.repository.account;

import com.finance_tracker.entity.Account;
import com.finance_tracker.enums.AccountType;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

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
