package com.finance_tracker.config;

import com.finance_tracker._shared.SystemConstants;
import com.finance_tracker.entity.Account;
import com.finance_tracker.entity.User;
import com.finance_tracker.enums.AccountType;
import com.finance_tracker.repository.AccountRepository;
import com.finance_tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class SystemInitializer implements CommandLineRunner {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    @Override
    public void run(String... args) throws Exception {
        User anonymousUser = (User) userRepository.findByEmail(SystemConstants.ANONYMOUS_USER_EMAIL)
                .orElseGet(() -> {
                    User newAnonymousUser = new User();
                    newAnonymousUser.setEmail(SystemConstants.ANONYMOUS_USER_EMAIL);
                    newAnonymousUser.setFirstName("Anonymous");
                    newAnonymousUser.setLastName("User");
                    newAnonymousUser.setPassword(SystemConstants.ANONYMOUS_USER_PASS);
                    return userRepository.save(newAnonymousUser);
                });
        accountRepository.findByName(SystemConstants.ANONYMOUS_ACCOUNT)
                .orElseGet(() -> {
                    Account account = new Account(anonymousUser, BigDecimal.ZERO);
                    account.setType(AccountType.SYSTEM);
                    account.setName(SystemConstants.ANONYMOUS_ACCOUNT);
                    accountRepository.save(account);
                    return account;
                });
    }
}
