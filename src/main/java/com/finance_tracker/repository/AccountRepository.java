package com.finance_tracker.repository;

import com.finance_tracker.entity.Account;
import com.finance_tracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID>, JpaSpecificationExecutor<Account> {
    Optional<Account> findByUserAndId(User user, UUID id);
    Optional<Account> findByName(String name);
}
