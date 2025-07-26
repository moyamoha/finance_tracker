package com.finance_tracker.repository.account;

import com.finance_tracker.entity.Account;
import com.finance_tracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID>, JpaSpecificationExecutor<Account> {
    Optional<Account> findByUserAndId(User user, UUID id);
    Optional<Account> findByName(String name);
    Boolean existsByUserAndName(User user, String name);
    void deleteByUser(User user);

    @Query("SELECT SUM(a.balance) FROM Account a WHERE a.user = :user")
    BigDecimal totalBalanceByUser(User user);
}
