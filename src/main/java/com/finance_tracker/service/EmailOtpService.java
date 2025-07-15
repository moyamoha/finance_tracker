package com.finance_tracker.service;

import com.finance_tracker.entity.TemporaryToken;
import com.finance_tracker.entity.User;
import com.finance_tracker.enums.TempTokenType;
import com.finance_tracker.repository.temporary_token.TemporaryTokenRepository;
import com.finance_tracker.service.mailing.EmailPayload;
import com.finance_tracker.service.mailing.MailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailOtpService {

    @Value("${app.security.otp-code-expiration-minutes}")
    private Integer otpExpirationMinutes;

    private final MailService mailService;
    private final TemporaryTokenRepository tokenRepository;

    public void generateAndSendOtp(User user) throws MessagingException {
        String otp = String.format("%06d", new Random().nextInt(999999));
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(2);

        TemporaryToken otpToken = new TemporaryToken();
        otpToken.setTokenKey(user.getEmail());
        otpToken.setValue(otp);
        otpToken.setTokenType(TempTokenType.OTP_CODE);
        otpToken.setExpiresAt(expiryTime);
        tokenRepository.save(otpToken);

        sendOtpEmailToUser(user, otp);
    }

    public boolean verifyOtp(String email, String otpCode) {
        Optional<TemporaryToken> maybeToken = tokenRepository.findByTokenKeyAndValueAndTokenTypeAndExpiresAtAfter(
                email,
                otpCode,
                TempTokenType.OTP_CODE,
                LocalDateTime.now()
        );
        if (maybeToken.isEmpty()) return false;
        tokenRepository.delete(maybeToken.get());
        return true;
    }

    public void sendOtpEmailToUser(User user, String otpCode) throws MessagingException {
        Map<String, Object> variables = new HashMap<>();
        variables.put("userName", user.getFirstName() + " " + user.getLastName());
        variables.put("otpCode", otpCode);
        variables.put("expirationMinutes", this.otpExpirationMinutes.toString());
        EmailPayload payload = new EmailPayload(
                user.getEmail(),
                "Verification code",
                variables
        );
        mailService.sendHtmlEmailFromTemplate("otp-email", payload);
    }
}
