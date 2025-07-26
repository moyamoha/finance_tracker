package com.finance_tracker.events.account.listeners;

import com.finance_tracker._shared.SystemConstants;
import com.finance_tracker.annotations.Auditable;
import com.finance_tracker.entity.Account;
import com.finance_tracker.entity.Transaction;
import com.finance_tracker.entity.User;
import com.finance_tracker.enums.AuditResourceType;
import com.finance_tracker.events.account.events.AccountDeletedEvent;
import com.finance_tracker.repository.account.AccountRepository;
import com.finance_tracker.repository.transaction.TransactionRepository;
import com.finance_tracker.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AccountDeletedListener {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private static final String AUDIT_ACTION_TYPE = "ANONYMIZE_TRANSACTIONS_ON_ACCOUNT_DELETION";

    @EventListener
    @Auditable(actionType = AUDIT_ACTION_TYPE, resourceType = AuditResourceType.TRANSACTION)
    public Object handleAccountDeleted(AccountDeletedEvent event) {
        Account deletedAccount = event.account();

        User anonymousUser = userRepository.findByEmail(SystemConstants.ANONYMOUS_USER_EMAIL).orElse(null);
        Account anonymousAccount = accountRepository.findByName(SystemConstants.ANONYMOUS_ACCOUNT).orElse(null);
        if (anonymousUser == null || anonymousAccount == null) {
            return Collections.emptyList();
        }

        List<Transaction> transactions = transactionRepository.findByAccount(deletedAccount);
        for (Transaction transaction : transactions) {
            transaction.setUser(anonymousUser);
            transaction.setAccount(anonymousAccount);
        }
        transactionRepository.saveAll(transactions);
        return transactions.stream().map(Transaction::getId).map(UUID::toString).toList();
    }
}
