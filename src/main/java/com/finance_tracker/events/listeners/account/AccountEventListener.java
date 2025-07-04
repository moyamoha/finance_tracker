package com.finance_tracker.events.listeners.account;

import com.finance_tracker._shared.SystemConstants;
import com.finance_tracker.entity.Account;
import com.finance_tracker.entity.Transaction;
import com.finance_tracker.entity.User;
import com.finance_tracker.events.events.account.AccountDeletedEvent;
import com.finance_tracker.repository.AccountRepository;
import com.finance_tracker.repository.TransactionRepository;
import com.finance_tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AccountEventListener {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @EventListener
    public void handleAccountDeleted(AccountDeletedEvent event) {
        Account deletedAccount = event.account();
        User anonymousUser = userRepository.findByEmail(SystemConstants.ANONYMOUS_USER_EMAIL).orElse(null);
        Account anonymousAccount = accountRepository.findByName(SystemConstants.ANONYMOUS_ACCOUNT).orElse(null);
        if (anonymousUser == null || anonymousAccount == null) { return; }

        List<Transaction> transactions = transactionRepository.findByAccount(deletedAccount);

        for (Transaction transaction : transactions) {
            transaction.setUser(anonymousUser);
            transaction.setAccount(anonymousAccount);
        }

        transactionRepository.saveAll(transactions);
    }
}
