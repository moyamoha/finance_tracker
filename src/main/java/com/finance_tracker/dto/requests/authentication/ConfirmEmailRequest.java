package com.finance_tracker.dto.requests.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ConfirmEmailRequest {
    @NotBlank
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank
    private String token;
}
