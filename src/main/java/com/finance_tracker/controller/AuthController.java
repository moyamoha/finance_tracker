package com.finance_tracker.controller;

import com.finance_tracker.authentication.AuthenticationService;
import com.finance_tracker.dto.requests.LoginRequest;
import com.finance_tracker.dto.requests.CreateUserRequest;
import com.finance_tracker.entity.User;
import com.finance_tracker.mapper.UserMapper;
import com.finance_tracker.repository.UserRepository;
import jakarta.validation.Valid;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;

@RestController()
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationService authService;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    public AuthController(AuthenticationService authService, UserRepository userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public String login(@RequestBody @Valid LoginRequest dto) {
        logger.info(dto.getEmail());
        return authService.login(dto);
    }

    @PostMapping("/signup")
    public void createUser(@Valid @RequestBody(required = true) CreateUserRequest dto) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(dto.getPassword());
        dto.setPassword(hashedPassword);
        User newUser = UserMapper.toEntity(dto);
        userRepository.save(newUser);
    }
}
