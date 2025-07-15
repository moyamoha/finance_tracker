package com.finance_tracker.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
@AllArgsConstructor
public class ProfileResponse {
    private String email;
    private String firstName;
    private String lastName;
    private Boolean multiFactorAuthEnabled;
}
