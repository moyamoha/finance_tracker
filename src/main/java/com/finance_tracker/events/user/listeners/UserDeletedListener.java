package com.finance_tracker.events.user.listeners;

import com.finance_tracker.entity.User;
import com.finance_tracker.events.user.events.UserDeletedEvent;
import com.finance_tracker.service.mailing.EmailPayload;
import com.finance_tracker.service.mailing.MailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.format.datetime.standard.DateTimeFormatterFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class UserDeletedListener {
    private final MailService mailService;

    @TransactionalEventListener
    @Async
    public void handleUserDeleted(UserDeletedEvent event) throws MessagingException {
        User user = event.user();
        sendGoodByeEmail(user);
    }

    private void sendGoodByeEmail(User user) throws MessagingException {
        Map<String, Object> variables = new HashMap<>();
        variables.put("userName", user.getFirstName() + " " + user.getLastName());
        variables.put("newAccountUrl", "https://yahyasalimi.com/signup");
        variables.put("userEmail", user.getEmail());
        variables.put("supportEmail", "support@yahyasalimi.com");

        EmailPayload payload = new EmailPayload(user.getEmail(), "Your account is being scheduled for deletion", variables);

        mailService.sendHtmlEmailFromTemplate("goodbye-email", payload);
    }
}
