package com.finance_tracker.events.user.listeners;

import com.finance_tracker._shared.SystemConstants;
import com.finance_tracker._shared.TokenGenerator;
import com.finance_tracker.entity.TemporaryToken;
import com.finance_tracker.entity.User;
import com.finance_tracker.enums.TempTokenType;
import com.finance_tracker.events.user.events.UserCreatedEvent;
import com.finance_tracker.events.user.events.UserRegisteredSuccessfullyEvent;
import com.finance_tracker.repository.temporary_token.TemporaryTokenRepository;
import com.finance_tracker.repository.user.UserRepository;
import com.finance_tracker.service.mailing.EmailPayload;
import com.finance_tracker.service.mailing.MailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class UserRegistrationListener {

    private final MailService mailService;
    private final TemporaryTokenRepository tokenRepository;
    private final UserRepository userRepository;

    @Value("${app.security.email-verification-token-expiration-hours}")
    private Integer emailVerificationTokenExpirationHours;

    @Value("${app.security.email-verification-token-length}")
    private Integer emailVerificationTokenLength;

    public UserRegistrationListener(MailService mailService, TemporaryTokenRepository tokenRepository, UserRepository userRepository) {
        this.mailService = mailService;
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    @EventListener
    @Async
    public void handleUserSignedUp(UserCreatedEvent event) throws MessagingException {
        User user = event.user();
        if (user.getEmail().equals(SystemConstants.ANONYMOUS_USER_EMAIL)) {
            user.setEmailConfirmed(true);
            userRepository.save(user);
            return;
        }

        TemporaryToken token = new TemporaryToken();
        token.setTokenType(TempTokenType.EMAIL_VERIFICATION);
        token.setTokenKey(user.getEmail());
        token.setValue(TokenGenerator.generateHexToken(emailVerificationTokenLength));
        token.setExpiresAt(LocalDateTime.now().plusHours(emailVerificationTokenExpirationHours));
        tokenRepository.save(token);

        sendConfirmationEmail(user, token);
    }

    @EventListener
    @Async
    public void handleUserRegistrationCompleted(UserRegisteredSuccessfullyEvent event) throws MessagingException {
        User user = event.user();
        if (user.getEmail().equals(SystemConstants.ANONYMOUS_USER_EMAIL)) return;
        tokenRepository.delete(event.token());
        sendWelcomeEmail(user);
    }

    private void sendWelcomeEmail(User user) throws MessagingException {
        Map<String, Object> variables = new HashMap<>();
        variables.put("userName", user.getFirstName() + " " + user.getLastName());
        variables.put("userEmail", user.getEmail());
        variables.put("loginUrl", "https://yahyasalimi.com/login"); // Example URL
        variables.put("currentYear", java.time.Year.now().getValue());
        EmailPayload payload = new EmailPayload(
                user.getEmail(),
                "Welcome",
                variables
        );
        mailService.sendHtmlEmailFromTemplate("welcome-email", payload);
    }

    private void sendConfirmationEmail(User user, TemporaryToken token) throws MessagingException {
        Map<String, Object> variables = new HashMap<>();
        variables.put("userName", user.getFirstName() + " " + user.getLastName());
        String verificationUrl = "http://localhost:8080/auth/confirm-email?token=" + token.getValue();
        variables.put("verificationUrl", verificationUrl);
        variables.put("linkExpirationHours", emailVerificationTokenExpirationHours);
        EmailPayload payload = new EmailPayload(
                user.getEmail(),
                "Complete your sign up",
                variables
        );
        mailService.sendHtmlEmailFromTemplate("confirm-email", payload);
    }
}
