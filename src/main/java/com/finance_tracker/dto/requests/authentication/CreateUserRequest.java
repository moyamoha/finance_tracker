package com.finance_tracker.dto.requests.authentication;

import com.finance_tracker._shared.custom_validation.ValidCurrency;
import com.finance_tracker._shared.custom_validation.ValidTimezone;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateUserRequest {

    @NotBlank()
    private String firstName;

    @NotBlank()
    private String lastName;

    @NotBlank()
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank()
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @ValidCurrency
    private String currency;

    @ValidTimezone
    private String timezone;

}
