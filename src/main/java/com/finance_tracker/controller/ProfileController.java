package com.finance_tracker.controller;

import com.finance_tracker.annotations.Auditable;
import com.finance_tracker.authentication.CustomUserDetails;
import com.finance_tracker.dto.requests.user.ChangeMfaSettingsRequest;
import com.finance_tracker.dto.requests.user.ChangeUserPreferencesRequest;
import com.finance_tracker.dto.requests.user.DeactivateUserAccountRequest;
import com.finance_tracker.dto.responses.user.ProfileResponse;
import com.finance_tracker.entity.User;
import com.finance_tracker.enums.AuditResourceType;
import com.finance_tracker.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
    public ProfileResponse fetchProfile(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        return new ProfileResponse(
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.isMfaEnabled(),
                user.getCurrency(),
                user.getTimezone(),
                user.getJoinedAt().toString()
        );
    }

    @PutMapping("/toggle-mfa")
    @Auditable(actionType = "CHANGE_MFA_SETTING", resourceType = AuditResourceType.USER)
    public void toggleMfa(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody ChangeMfaSettingsRequest dto
    ) {
        User user = userDetails.getUser();
        userService.changeMfaSetting(user, dto);
    }

    @PatchMapping("/deactivate")
    @Auditable(actionType = "DEACTIVATE_USER_ACCOUNT", resourceType = AuditResourceType.USER)
    public void deactivateAccount(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody DeactivateUserAccountRequest dto
    ) {
        userService.deactivateUserAccount(userDetails.getUser(), dto.getPassword());
    }

    @PutMapping("/preferences")
    @Auditable(actionType = "CHANGE_USER_PREFERENCES", resourceType = AuditResourceType.USER)
    public ProfileResponse changePreferences(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody ChangeUserPreferencesRequest dto
    ) {
        User user = userService.changeUserPreferences(userDetails.getUser(), dto);
        return new ProfileResponse(
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.isMfaEnabled(),
                user.getCurrency(),
                user.getTimezone(),
                user.getJoinedAt().toString()
        );
    }
}