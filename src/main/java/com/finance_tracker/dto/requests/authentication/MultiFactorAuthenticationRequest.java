package com.finance_tracker.dto.requests.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MultiFactorAuthenticationRequest {
    @NotBlank
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank
    private String otpCode;
}
