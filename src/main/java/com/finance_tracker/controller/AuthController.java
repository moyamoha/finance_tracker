package com.finance_tracker.controller;

import com.finance_tracker.authentication.AuthenticationService;
import com.finance_tracker.dto.requests.authentication.ConfirmEmailRequest;
import com.finance_tracker.dto.requests.authentication.LoginRequest;
import com.finance_tracker.dto.requests.authentication.CreateUserRequest;
import com.finance_tracker.dto.requests.authentication.MultiFactorAuthenticationRequest;
import com.finance_tracker.dto.responses.authentication.SuccessfulLoginTokenResponse;
import com.finance_tracker.repository.UserRepository;
import com.finance_tracker.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/auth")
@Tag(name = "Authentication")
public class AuthController {

    private final AuthenticationService authService;
    private final UserService userService;

    public AuthController(AuthenticationService authService, UserRepository userRepository, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public Object login(@RequestBody @Valid LoginRequest dto) throws MessagingException {
        return authService.login(dto);
    }

    @PostMapping("/signup")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public void createUser(@Valid @RequestBody(required = true) CreateUserRequest dto) {
        userService.createUser(dto);
    }

    @PostMapping("/confirm-email")
    public void completeRegistration(@Valid @RequestBody ConfirmEmailRequest dto) {
        authService.confirmEmail(dto);
    }

    @PostMapping("/verify-otp")
    public SuccessfulLoginTokenResponse verifyOtp(@Valid @RequestBody(required = true) MultiFactorAuthenticationRequest dto) {
        return authService.verifyOtp(dto);
    }
}
