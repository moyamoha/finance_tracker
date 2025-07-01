package com.finance_tracker.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUserRequest {

    @NotBlank()
    private String firstName;

    @NotBlank()
    private String lastName;

    @NotBlank() @Email(message = "Invalid email format")
    private String email;

    @NotBlank() @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
}
