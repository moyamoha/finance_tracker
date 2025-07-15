package com.finance_tracker.helpers;

import com.finance_tracker.entity.Account;
import com.finance_tracker.entity.Transaction;
import com.finance_tracker.enums.TransactionType;
import com.finance_tracker.events.transaction.TransactionUpdateInfo;
import com.finance_tracker.exception.custom.account.InsufficientFundsException;

import java.math.BigDecimal;

public class AccountHelper {

    public static void adjustBalanceForNewTransaction(Account account, Transaction transaction) {
        if (transaction.getType().equals(TransactionType.INCOME)) {
            account.incrementBalance(transaction.getAmount());
        } else {
            account.decrementBalance(transaction.getAmount());
        }
    }

    public static void adjustBalanceOnTransactionDeleted(Account account, Transaction transaction) {
        if (transaction.getType().equals(TransactionType.INCOME)) {
            account.decrementBalance(transaction.getAmount());
        } else {
            account.incrementBalance(transaction.getAmount());
        }
    }

    public static void adjustBalanceOnTransactionUpdated(
            Account account,
            Transaction updated,
            Transaction original
    ) {
        adjustBalanceOnTransactionDetachedFromAccount(account, original);
        adjustBalanceForNewTransaction(account, updated);
    }

    public static void adjustBalanceOnTransactionDetachedFromAccount(Account account, Transaction transaction) {
        if (transaction.getType().equals(TransactionType.INCOME)) {
            account.decrementBalance(transaction.getAmount());
        } else {
            account.incrementBalance(transaction.getAmount());
        }
    }

    public static void validateSufficientFundsInAccount(Account account) {
        if (account.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw InsufficientFundsException.withAccountId(account.getId());
        }
    }
}
