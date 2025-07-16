package com.finance_tracker.dto.responses.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
@AllArgsConstructor
public class WholeProfileResponse {
    private String email;
    private String firstName;
    private String lastName;
    private Boolean multiFactorAuthEnabled;
    private UserProfileResponse preferences;
}
