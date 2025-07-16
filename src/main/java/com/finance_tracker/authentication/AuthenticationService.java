package com.finance_tracker.authentication;

import com.finance_tracker._shared.AppConstants;
import com.finance_tracker.dto.requests.authentication.ConfirmEmailRequest;
import com.finance_tracker.dto.requests.authentication.LoginRequest;
import com.finance_tracker.dto.requests.authentication.MultiFactorAuthenticationRequest;
import com.finance_tracker.dto.responses.authentication.MultiFactorAuthRequiredResponse;
import com.finance_tracker.dto.responses.authentication.SuccessfulLoginTokenResponse;
import com.finance_tracker.entity.TemporaryToken;
import com.finance_tracker.entity.User;
import com.finance_tracker.enums.TempTokenType;
import com.finance_tracker.events.user.events.ReactivateMarkedUserEvent;
import com.finance_tracker.events.user.events.UserRegisteredSuccessfullyEvent;
import com.finance_tracker.exception.authentication.InvalidEmailOrPasswordException;
import com.finance_tracker.exception.authentication.InvalidOrExpiredToken;
import com.finance_tracker.exception.authentication.OtpCodeIncorrectOrExpiredException;
import com.finance_tracker.exception.authentication.UserNotFoundException;
import com.finance_tracker.repository.user.UserRepository;
import com.finance_tracker.repository.temporary_token.TemporaryTokenRepository;
import com.finance_tracker.service.EmailOtpService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final TemporaryTokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;
    private final ApplicationEventPublisher eventPublisher;
    private final EmailOtpService otpService;

    public Object login (LoginRequest dto) throws MessagingException {
        Authentication authentication;
        try {
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword());
            authentication = authenticationManager.authenticate(authToken);
        } catch (AuthenticationException e) {
            if (e instanceof BadCredentialsException) {
                throw new InvalidEmailOrPasswordException("Invalid email or password.");
            } else {
                throw new InvalidEmailOrPasswordException("Authentication failed unexpectedly: " + e.getMessage());
            }
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String authenticatedEmail = userDetails.getUsername(); // This gives you the email/username

        User user = userRepository.findByEmail(authenticatedEmail).get() ;
        if (!user.getEmailConfirmed()) {
            throw new InvalidEmailOrPasswordException("Email is not confirmed yet");
        }
        user.setLastLoggedIn(LocalDateTime.now());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        if (user.getIsMfaEnabled()) {
            otpService.generateAndSendOtp(user);
            userRepository.save(user);
            return new MultiFactorAuthRequiredResponse(
                    AppConstants.OTP_REQUIRED_MESSAGE,
                    true,
                    user.getEmail()
            );
        } else {
            if (user.getMarkedInactiveAt() != null) {
                user.setMarkedInactiveAt(null);
                userRepository.save(user);
                eventPublisher.publishEvent(new ReactivateMarkedUserEvent(user));
            }
            String token = jwtService.generateToken(authentication.getName());
            return new SuccessfulLoginTokenResponse(token);
        }
    }

    public SuccessfulLoginTokenResponse verifyOtp(MultiFactorAuthenticationRequest dto) {
        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(
                () -> new UserNotFoundException("User was not found")
        );
        user.setLastLoggedIn(LocalDateTime.now());
        user.setMarkedInactiveAt(null);

        boolean isOtpCorrect = otpService.verifyOtp(user.getEmail(), dto.getOtpCode());
        if (isOtpCorrect) {
            String token = jwtService.generateToken(user.getEmail());
            userRepository.save(user);
            return new SuccessfulLoginTokenResponse(token);
        } else {
            throw new OtpCodeIncorrectOrExpiredException(dto.getOtpCode());
        }
    }

    public void confirmEmail(ConfirmEmailRequest dto) {
        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(
                () -> new UserNotFoundException(String.format("User '%s' was not found", dto.getEmail()))
        );
        Optional<TemporaryToken> maybeToken =
                tokenRepository.findByTokenKeyAndValueAndTokenTypeAndExpiresAtAfter(
                        dto.getEmail(),
                        dto.getToken(),
                        TempTokenType.EMAIL_VERIFICATION,
                        LocalDateTime.now()
                );
        if (maybeToken.isEmpty()) {
            throw new InvalidOrExpiredToken();
        }
        user.setEmailConfirmed(true);
        userRepository.save(user);
        eventPublisher.publishEvent(new UserRegisteredSuccessfullyEvent(user, maybeToken.get()));
    }
}
