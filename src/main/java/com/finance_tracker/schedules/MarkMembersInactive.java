package com.finance_tracker.schedules;

import com.finance_tracker.annotations.Auditable;
import com.finance_tracker.entity.User;
import com.finance_tracker.enums.AuditResourceType;
import com.finance_tracker.events.user.events.UserMarkedForDeletionEvent;
import com.finance_tracker.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class MarkMembersInactive {

    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Scheduled(cron = "0 0 1 * * *")
    @Auditable(actionType = "MARK_USERS_INACTIVE", resourceType = AuditResourceType.USER)
    public Object markUsersInactive() {
        try {
            LocalDateTime twoYearsAgo = LocalDateTime.now().minusYears(2);
            List<User> usersEligibleToBeMarkedInactive = userRepository
                    .findByMarkedInactiveAtAndLastLoggedInBefore(null, twoYearsAgo);
            usersEligibleToBeMarkedInactive.forEach((user -> {
                user.setMarkedInactiveAt(LocalDateTime.now());
                eventPublisher.publishEvent(new UserMarkedForDeletionEvent(user));
            }));
            userRepository.saveAll(usersEligibleToBeMarkedInactive);
            int count = usersEligibleToBeMarkedInactive.size();
            Map<String, Object> result = new HashMap<>();
            result.put("count", count);
            return result;
        } catch (Exception e) {
            return null;
            // System.out.println(e.getMessage());
        }
    }
}
