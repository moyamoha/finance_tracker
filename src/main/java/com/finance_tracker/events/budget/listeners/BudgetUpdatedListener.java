package com.finance_tracker.events.budget.listeners;

import com.finance_tracker.dto.responses.transaction.TransactionResponse;
import com.finance_tracker.entity.Budget;
import com.finance_tracker.entity.Transaction;
import com.finance_tracker.events.budget.events.BudgetUpdatedEvent;
import com.finance_tracker.repository.budget.BudgetRepository;
import com.finance_tracker.repository.transaction.TransactionRepository;
import com.finance_tracker.service.BudgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BudgetUpdatedListener {

    private final BudgetService budgetService;
    private final BudgetRepository budgetRepository;
    private final TransactionRepository transactionRepository;

    @TransactionalEventListener()
    public void handleBudgetUpdated(BudgetUpdatedEvent event) {
        List<Transaction> transactions = transactionRepository.findForBudget(event.updated());
        BigDecimal total = transactions.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        Budget budget = event.updated();
        budget.setRemaining(budget.getAmount().subtract(total));
    }
}
