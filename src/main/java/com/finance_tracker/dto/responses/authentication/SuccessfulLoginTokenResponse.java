package com.finance_tracker.dto.responses.authentication;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class SuccessfulLoginTokenResponse {
    @Getter @Setter
    private String token;
}
