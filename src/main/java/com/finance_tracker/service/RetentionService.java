package com.finance_tracker.service;

import com.finance_tracker.entity.User;
import com.finance_tracker.repository.account.AccountRepository;
import com.finance_tracker.repository.budget.BudgetRepository;
import com.finance_tracker.repository.temporary_token.TemporaryTokenRepository;
import com.finance_tracker.repository.transaction.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RetentionService {

    private final AccountRepository accountRepository;
    private final BudgetRepository budgetRepository;
    private final TransactionRepository transactionRepository;
    private final TemporaryTokenRepository tokenRepository;

    public void deleteUserData(User user) {
        budgetRepository.deleteByUser(user);
        transactionRepository.deleteByUser(user);
        accountRepository.deleteByUser(user);
        tokenRepository.deleteByTokenKey(user.getEmail());
    }
}
