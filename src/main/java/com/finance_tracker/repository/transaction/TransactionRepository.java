package com.finance_tracker.repository.transaction;

import com.finance_tracker.entity.Account;
import com.finance_tracker.entity.Transaction;
import com.finance_tracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository
        extends JpaRepository<Transaction, UUID>, JpaSpecificationExecutor<Transaction>, TransactionRepositoryCustom {
    Optional<Transaction> getTransactionsByUserAndId(User user, UUID id);
    List<Transaction> findByAccount(Account account);
    void deleteByUser(User user);
}
