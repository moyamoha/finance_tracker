package com.finance_tracker.schedules;

import com.finance_tracker.annotations.Auditable;
import com.finance_tracker.entity.RecurringTransaction;
import com.finance_tracker.enums.AuditResourceType;
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

    private final static String ACTION_TYPE = "GENERATE_TRANSACTIONS_FROM_RECURRING_TRANSACTION";
    private final TransactionService transactionService;
    private final RecurringTransactionRepository recurringTransactionRepository;
    private final RecurringTransactionService recurringTransactionService;

    @Scheduled(cron = "0 0 4 * * *")
    @Auditable(actionType = ACTION_TYPE, includeResult = false, includeArgs = false, resourceType = AuditResourceType.TRANSACTION)
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
