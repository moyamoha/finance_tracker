package com.finance_tracker.events.listeners.transaction;

import com.finance_tracker.entity.Account;
import com.finance_tracker.entity.Transaction;
import com.finance_tracker.enums.TransactionType;
import com.finance_tracker.events.events.transaction.TransactionCreatedEvent;
import com.finance_tracker.events.events.transaction.TransactionDeletedEvent;
import com.finance_tracker.events.events.transaction.TransactionUpdatedEvent;
import com.finance_tracker.events.payload.TransactionUpdateInfo;
import com.finance_tracker.exception.custom.account.InsufficientFundsException;
import com.finance_tracker.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class TransactionEventListener {
    private final AccountRepository accountRepository;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleTransactionCreated(TransactionCreatedEvent event) {
        Transaction transaction = event.transaction();

        Account account = transaction.getAccount();
        if (transaction.getType().equals(TransactionType.INCOME)) {
            account.incrementBalance(transaction.getAmount());
        } else {
            account.decrementBalance(transaction.getAmount());
        }
        validateSufficientFundsInAccount(account);
        accountRepository.save(account);
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleTransactionDeleted(TransactionDeletedEvent event) {
        Transaction transaction = event.transaction();
        adjustBalanceForNewTransaction(transaction);
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleTransactionUpdated(TransactionUpdatedEvent event) {
        Transaction transaction = event.transaction();

        Account account = transaction.getAccount();
        TransactionUpdateInfo oldBalance = event.oldInfo();

        if (oldBalance.type().equals(TransactionType.INCOME)) {
            account.decrementBalance(oldBalance.amount());
        } else {
            account.incrementBalance(oldBalance.amount());
        }

        adjustBalanceForNewTransaction(transaction);
        validateSufficientFundsInAccount(account);
    }

    private void adjustBalanceForNewTransaction(Transaction transaction) {
        Account account = transaction.getAccount();
        if (transaction.getType().equals(TransactionType.INCOME)) {
            account.incrementBalance(transaction.getAmount());
        } else {
            account.decrementBalance(transaction.getAmount());
        }
    }

    private void validateSufficientFundsInAccount(Account account) {
        if (account.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw InsufficientFundsException.withAccountId(account.getId());
        }
    }
}
