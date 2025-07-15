package com.finance_tracker.events.transaction.listeners;

import com.finance_tracker.entity.Account;
import com.finance_tracker.entity.Budget;
import com.finance_tracker.entity.Transaction;
import com.finance_tracker.enums.TransactionType;
import com.finance_tracker.events.transaction.events.TransactionUpdatedEvent;
import com.finance_tracker.helpers.AccountHelper;
import com.finance_tracker.repository.budget.BudgetRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import java.util.List;

@Component
@AllArgsConstructor
public class TransactionUpdatedListener {

    private final BudgetRepository budgetRepository;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleTransactionUpdated(TransactionUpdatedEvent event) {
        Transaction updated = event.updated();
        Transaction original = event.original();

        Account account = updated.getAccount();
        if (updated.getAccount().getId().equals(original.getAccount().getId())) {
            AccountHelper.adjustBalanceOnTransactionUpdated(account, updated, original);
        } else {
            AccountHelper.adjustBalanceOnTransactionDetachedFromAccount(original.getAccount(), original);
            AccountHelper.adjustBalanceForNewTransaction(account, updated);
        }

        syncBudgetsForOldTransaction(original);
        syncBudgetsForNewTransaction(updated);

        AccountHelper.validateSufficientFundsInAccount(account);
    }

    private void syncBudgetsForOldTransaction(Transaction transaction) {
        List<Budget> budgets = budgetRepository.findForTransaction(transaction);
        budgets.forEach(budget -> {
            budget.incrementRemaining(transaction.getAmount());
        });
    }

    private void syncBudgetsForNewTransaction(Transaction transaction) {
        List<Budget> budgets = budgetRepository.findForTransaction(transaction);
        budgets.forEach(budget -> {
            budget.decrementRemaining(transaction.getAmount());
        });
    }
}
