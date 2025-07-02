package com.finance_tracker.controller;

import com.finance_tracker.authentication.AuthenticationService;
import com.finance_tracker.dto.requests.LoginRequest;
import com.finance_tracker.dto.requests.CreateUserRequest;
import com.finance_tracker.repository.UserRepository;
import com.finance_tracker.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public String login(@RequestBody @Valid LoginRequest dto) {
        return authService.login(dto);
    }

    @PostMapping("/signup")
    public void createUser(@Valid @RequestBody(required = true) CreateUserRequest dto) {
        userService.createUser(dto);
    }
}
