package com.finance_tracker.repository;

import com.finance_tracker.entity.Transaction;
import com.finance_tracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository
        extends JpaRepository<Transaction, UUID>, JpaSpecificationExecutor<Transaction> {
    Transaction getTransactionsById(UUID id);

    Optional<Transaction> getTransactionsByUserAndId(User user, UUID id);
}
