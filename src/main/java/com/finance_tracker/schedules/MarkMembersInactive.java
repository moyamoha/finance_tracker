package com.finance_tracker.schedules;

import com.finance_tracker.entity.User;
import com.finance_tracker.events.user.events.UserMarkedForDeletionEvent;
import com.finance_tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MarkMembersInactive {

    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Scheduled(cron = "0 0 2 * * *")
    public void markUsersInactive() {
        try {
            LocalDateTime twoYearsAgo = LocalDateTime.now().minusYears(2);
            List<User> usersEligibleToBeMarkedInactive = userRepository
                    .findByMarkedInactiveAtAndLastLoggedInBefore(null, twoYearsAgo);
            usersEligibleToBeMarkedInactive.forEach((user -> {
                user.setMarkedInactiveAt(LocalDateTime.now());
                eventPublisher.publishEvent(new UserMarkedForDeletionEvent(user));
            }));
            userRepository.saveAll(usersEligibleToBeMarkedInactive);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
