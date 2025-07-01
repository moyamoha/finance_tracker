package com.finance_tracker.controller;

import com.finance_tracker.authentication.CustomUserDetailsService;
import com.finance_tracker.dto.responses.ProfileResponse;
import com.finance_tracker.entity.User;
import com.finance_tracker.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/me")
    public ProfileResponse fetchProfile(@AuthenticationPrincipal User user) {
        logger.info(user.getId().toString());
        return new ProfileResponse(
                user.getEmail(),
                user.getFirstName(),
                user.getLastName()
        );
    }

}