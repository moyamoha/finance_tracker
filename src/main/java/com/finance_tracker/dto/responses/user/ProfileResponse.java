package com.finance_tracker.dto.responses.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter @Getter
@AllArgsConstructor
public class ProfileResponse implements Serializable {
    private String email;
    private String firstName;
    private String lastName;
    private boolean multiFactorAuthEnabled;
    private String currency;
    private String timezone;
    private String joinedAt;
}
