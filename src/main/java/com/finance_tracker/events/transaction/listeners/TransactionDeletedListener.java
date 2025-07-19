package com.finance_tracker.events.transaction.listeners;

import com.finance_tracker.entity.Account;
import com.finance_tracker.entity.Budget;
import com.finance_tracker.entity.Transaction;
import com.finance_tracker.enums.TransactionType;
import com.finance_tracker.events.transaction.events.TransactionDeletedEvent;
import com.finance_tracker.helpers.AccountHelper;
import com.finance_tracker.repository.budget.BudgetRepository;
import com.finance_tracker.service.validators.AccountValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Component
@AllArgsConstructor
public class TransactionDeletedListener {

    private final BudgetRepository budgetRepository;
    private final AccountValidator accountValidator;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleTransactionDeleted(TransactionDeletedEvent event) {
        Transaction transaction = event.transaction();
        Account account = transaction.getAccount();

        AccountHelper.adjustBalanceOnTransactionDeleted(account, transaction);
        accountValidator.validate(account);

        List<Budget> budgets = budgetRepository.findForTransaction(transaction);
        budgets.forEach(budget -> {
            budget.incrementRemaining(transaction.getAmount());
        });
    }
}
