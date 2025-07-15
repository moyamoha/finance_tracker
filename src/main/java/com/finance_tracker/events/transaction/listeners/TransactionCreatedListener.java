package com.finance_tracker.events.transaction.listeners;

import com.finance_tracker.entity.Account;
import com.finance_tracker.entity.Budget;
import com.finance_tracker.entity.Transaction;
import com.finance_tracker.events.transaction.events.TransactionCreatedEvent;
import com.finance_tracker.exception.custom.account.InsufficientFundsException;
import com.finance_tracker.helpers.AccountHelper;
import com.finance_tracker.repository.account.AccountRepository;
import com.finance_tracker.repository.budget.BudgetRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.math.BigDecimal;
import java.util.List;

@Component
@AllArgsConstructor
public class TransactionCreatedListener {

    private final BudgetRepository budgetRepository;
    private final AccountRepository accountRepository;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleTransactionCreated(TransactionCreatedEvent event) {

        Transaction transaction = event.transaction();
        Account account = transaction.getAccount();

        AccountHelper.adjustBalanceForNewTransaction(account, transaction);
        AccountHelper.validateSufficientFundsInAccount(account);

        List<Budget> budgets = budgetRepository.findForTransaction(transaction);
        budgets.forEach(budget -> {
            budget.decrementRemaining(transaction.getAmount());
            budgetRepository.save(budget);
        });
        accountRepository.save(account);
    }
}
