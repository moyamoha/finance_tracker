package com.finance_tracker.controller;

import com.finance_tracker.authentication.CustomUserDetailsService;
import com.finance_tracker.dto.responses.ProfileResponse;
import com.finance_tracker.entity.User;
import com.finance_tracker.repository.UserRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/users")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Users")
public class UserController {

    @GetMapping("/me")
    public ProfileResponse fetchProfile(@AuthenticationPrincipal User user) {
        return new ProfileResponse(
                user.getEmail(),
                user.getFirstName(),
                user.getLastName()
        );
    }
}