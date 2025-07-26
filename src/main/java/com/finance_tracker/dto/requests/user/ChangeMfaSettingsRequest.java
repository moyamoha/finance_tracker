package com.finance_tracker.dto.requests.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChangeMfaSettingsRequest {
    @NotBlank
    private String password;

    @NotNull
    private boolean isMfaEnabled;
}
