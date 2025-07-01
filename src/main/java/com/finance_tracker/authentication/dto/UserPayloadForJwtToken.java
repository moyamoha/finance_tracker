package com.finance_tracker.authentication.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserPayloadForJwtToken {

    private String email;
    private String firstName;
    private String lastName;
}
