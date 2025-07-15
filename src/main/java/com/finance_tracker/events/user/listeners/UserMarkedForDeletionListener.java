package com.finance_tracker.events.user.listeners;

import com.finance_tracker.entity.User;
import com.finance_tracker.events.user.events.UserMarkedForDeletionEvent;
import com.finance_tracker.service.mailing.EmailPayload;
import com.finance_tracker.service.mailing.MailService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class UserMarkedForDeletionListener {

    private final MailService mailService;

    @Value("${app.retention.notice-period-as-months}")
    private Integer noticePeriodAsMonths;

    @EventListener
    @Async
    public void handleUserMarkedForDeletion(UserMarkedForDeletionEvent event) throws MessagingException {
        notifyUserAboutTheDecision(event.user());
    }

    private void notifyUserAboutTheDecision(User user) throws MessagingException {
        Map<String, Object> variables = new HashMap<>();
        variables.put("userName", user.getFirstName() + " " + user.getLastName());
        variables.put("loginUrl", "https://yahyasalimi.com/login");
        variables.put("userEmail", user.getEmail());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy hh:mm");
        LocalDateTime aMonthFromNow = LocalDateTime.now().plusMonths(noticePeriodAsMonths);
        variables.put("deletionDate", aMonthFromNow.format(formatter));


        EmailPayload payload = new EmailPayload(user.getEmail(), "Your account is being scheduled for deletion", variables);

        mailService.sendHtmlEmailFromTemplate("user-scheduled-for-deletion-email", payload);
    }
}
