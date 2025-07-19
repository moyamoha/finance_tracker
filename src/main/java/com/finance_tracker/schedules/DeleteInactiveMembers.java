package com.finance_tracker.schedules;

import com.finance_tracker.entity.User;
import com.finance_tracker.events.user.events.UserDeletedEvent;
import com.finance_tracker.repository.user.UserRepository;
import com.finance_tracker.service.RetentionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DeleteInactiveMembers {

    private final UserRepository userRepository;
    private final RetentionService retentionService;
    private final ApplicationEventPublisher eventPublisher;

    @Value("${app.retention.notice-period-as-months}")
    private String noticePeriodAsMonths;

    @Scheduled(cron = "0 0 2 * * *")
    @Transactional
    public void deleteMarkedMembers() {
        try {
            List<User> eligibleUsers = userRepository.findByMarkedInactiveAtBefore(
                    LocalDateTime.now().minusMonths(Integer.parseInt(noticePeriodAsMonths))
            );
            eligibleUsers.forEach((user -> {
                retentionService.deleteUserData(user);
                userRepository.delete(user);
                eventPublisher.publishEvent(new UserDeletedEvent(user));
            }));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
