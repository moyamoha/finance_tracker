package com.finance_tracker.events.user.listeners;

import com.finance_tracker.entity.User;
import com.finance_tracker.events.user.events.ReactivateMarkedUserEvent;
import com.finance_tracker.service.mailing.EmailPayload;
import com.finance_tracker.service.mailing.MailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ReactivateMarkedUserListener {

    private final MailService mailService;

    @EventListener
    @Async
    public void handleUserReactivation(ReactivateMarkedUserEvent event) throws MessagingException {
        sendWelcomeBackEmail(event.user());
    }

    private void sendWelcomeBackEmail(User user) throws MessagingException {
        Map<String, Object> variables = new HashMap<>();
        variables.put("userName", user.getFirstName() + " " + user.getLastName());

        EmailPayload payload = new EmailPayload(
                user.getEmail(),
                "Welcome",
                variables
        );
        mailService.sendHtmlEmailFromTemplate("welcome-back-email", payload);
    }
}
