package com.finance_tracker.repository.budget;

import com.finance_tracker.entity.Account;
import com.finance_tracker.entity.Budget;
import com.finance_tracker.entity.User;
import com.finance_tracker.enums.BudgetPeriod;
import com.finance_tracker.enums.TransactionCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BudgetRepository extends JpaRepository<Budget, UUID>, JpaSpecificationExecutor<Budget>, BudgetRepositoryCustom {
    Optional<Budget> findByUserAndId(User user, UUID id);
    Boolean existsByUserAndAccountAndCategory(User user, Account account, TransactionCategory category);
    void deleteByUser(User user);
}
