package com.finance_tracker.schedules;

import com.finance_tracker.annotations.Auditable;
import com.finance_tracker.entity.User;
import com.finance_tracker.enums.AuditResourceType;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Auditable(actionType = "DELETE_INACTIVE_USERS", resourceType = AuditResourceType.MULTIPLE)
    public Object deleteMarkedMembers() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<User> eligibleUsers = userRepository.findByMarkedInactiveAtBefore(
                    LocalDateTime.now().minusMonths(Integer.parseInt(noticePeriodAsMonths))
            );
            eligibleUsers.forEach((user -> {
                retentionService.deleteUserData(user);
                userRepository.delete(user);
                eventPublisher.publishEvent(new UserDeletedEvent(user));
            }));
            result.put("count", eligibleUsers.size());
            return result;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return result;
        }
    }
}
