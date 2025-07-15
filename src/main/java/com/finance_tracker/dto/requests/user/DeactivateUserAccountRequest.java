package com.finance_tracker.dto.requests.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
@AllArgsConstructor
public class DeactivateUserAccountRequest {
    @NotBlank
    private String password;
}