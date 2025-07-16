package com.finance_tracker.controller;

import com.finance_tracker.authentication.CustomUserDetails;
import com.finance_tracker.dto.requests.user.ChangeMfaSettingsRequest;
import com.finance_tracker.dto.requests.user.ChangeUserPreferencesRequest;
import com.finance_tracker.dto.requests.user.DeactivateUserAccountRequest;
import com.finance_tracker.dto.responses.user.UserProfileResponse;
import com.finance_tracker.dto.responses.user.WholeProfileResponse;
import com.finance_tracker.entity.User;
import com.finance_tracker.entity.UserProfile;
import com.finance_tracker.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.websocket.MessageHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "User Profile")
public class ProfileController {

    private final UserService userService;

    @GetMapping("/")
    public WholeProfileResponse fetchProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        UserProfile userProfile = userDetails.getUserProfile();
        UserProfileResponse userProfileResponse = UserProfileResponse.builder()
                .currency(userProfile.getCurrency())
                .timezone(userProfile.getTimezone())
                .build();
        return new WholeProfileResponse(
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getIsMfaEnabled(),
                userProfileResponse
        );
    }

    @PutMapping("/toggle-mfa")
    public void toggleMfa(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody ChangeMfaSettingsRequest dto
    ) {
        User user = userDetails.getUser();
        userService.changeMfaSetting(user, dto);
    }

    @PatchMapping("/deactivate")
    public void deactivateAccount(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody DeactivateUserAccountRequest dto
    ) {
        userService.deactivateUserAccount(userDetails.getUser(), dto.getPassword());
    }

    @PutMapping("/preferences")
    public WholeProfileResponse changePreferences(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody ChangeUserPreferencesRequest dto
    ) {
        User user = userDetails.getUser();
        UserProfile profile = userDetails.getUserProfile();
        profile = userService.changeUserPreferences(profile, dto);
        UserProfileResponse userProfileResponse = UserProfileResponse.builder()
                .currency(profile.getCurrency())
                .timezone(profile.getTimezone())
                .build();
        return new WholeProfileResponse(
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getIsMfaEnabled(),
                userProfileResponse
        );
    }
}