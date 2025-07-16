package com.finance_tracker.schedules;

import com.finance_tracker.entity.RecurringTransaction;
import com.finance_tracker.helpers.RecurringTransactionHelper;
import com.finance_tracker.repository.recurring_transaction.RecurringTransactionRepository;
import com.finance_tracker.service.RecurringTransactionService;
import com.finance_tracker.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GenerateTransactionsFromRecurringTransactions {

    private final TransactionService transactionService;
    private final RecurringTransactionRepository recurringTransactionRepository;
    private final RecurringTransactionService recurringTransactionService;

    @Scheduled(cron = "0 30 1 * * *")
    public void handle() {
        LocalDate today = LocalDate.now();
        List<RecurringTransaction> eligible = recurringTransactionService.getByNextGenerationDate(today);
        eligible.forEach((recurringTransaction -> {
            transactionService.createFromRecurringTransaction(recurringTransaction);

            recurringTransaction.setLastGeneratedDate(recurringTransaction.getNextGenerationDate());
            LocalDate next = RecurringTransactionHelper.calculateNextGenerationDate(recurringTransaction);
            recurringTransaction.setNextGenerationDate(next);

            recurringTransactionRepository.save(recurringTransaction);
        }));
    }
}
