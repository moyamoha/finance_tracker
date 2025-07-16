package com.finance_tracker.config;

import com.finance_tracker._shared.SystemConstants;
import com.finance_tracker.dto.requests.authentication.CreateUserRequest;
import com.finance_tracker.entity.Account;
import com.finance_tracker.entity.User;
import com.finance_tracker.enums.AccountType;
import com.finance_tracker.repository.account.AccountRepository;
import com.finance_tracker.repository.user.UserRepository;
import com.finance_tracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class SystemInitializer implements CommandLineRunner {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Override
    public void run(String... args) throws Exception {
        User anonymousUser = (User) userRepository.findByEmail(SystemConstants.ANONYMOUS_USER_EMAIL)
                .orElseGet(() -> {
                    CreateUserRequest payload = CreateUserRequest.builder()
                            .email(SystemConstants.ANONYMOUS_USER_EMAIL)
                            .firstName("Anonymous")
                            .lastName("User")
                            .password(SystemConstants.ANONYMOUS_USER_PASS)
                            .build();
                    return userService.createUser(payload);
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
