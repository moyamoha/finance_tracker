package com.finance_tracker.schedules;

import com.finance_tracker.repository.user.UserRepository;
import com.finance_tracker.repository.temporary_token.TemporaryTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ClearUpDatabase {

    private final UserRepository userRepository;
    private final TemporaryTokenRepository tokenRepository;

    @Value("${app.security.email-verification-token-expiration-hours}")
    private Integer emailVerificationTokenExpirationHours;


    // EVERY DAY AT 3
    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    public void cleanUpDatabase() {
        deleteUsersWhoHaveNotConfirmedTheirEmail();
        deleteExpiredTokens();
    }

    private void deleteUsersWhoHaveNotConfirmedTheirEmail() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dateToCheck = now.minusHours(emailVerificationTokenExpirationHours).minusMinutes(1);
        userRepository.deleteByEmailConfirmedAndJoinedAtBefore(false, dateToCheck);
    }

    private void deleteExpiredTokens() {
        tokenRepository.deleteByExpiresAtBefore(LocalDateTime.now().minusMinutes(1));
    }
}
