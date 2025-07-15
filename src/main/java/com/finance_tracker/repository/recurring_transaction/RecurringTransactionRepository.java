package com.finance_tracker.repository.recurring_transaction;

import com.finance_tracker.dto.responses.recurring_transaction.RecurringTransactionCollectionResponse;
import com.finance_tracker.entity.RecurringTransaction;
import com.finance_tracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RecurringTransactionRepository extends JpaRepository<RecurringTransaction, UUID> {
    Optional<RecurringTransaction> findByUserAndId(User user, UUID id);
    void deleteByUserAndId(User user, UUID id);
    List<RecurringTransaction> findByNextGenerationDate(LocalDate nextGenerationDate);

    List<RecurringTransaction> findByUser(User user);
}
