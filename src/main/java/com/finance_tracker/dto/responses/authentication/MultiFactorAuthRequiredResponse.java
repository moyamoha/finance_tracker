package com.finance_tracker.dto.responses.authentication;

public record MultiFactorAuthRequiredResponse(
        String message,
        boolean multiFactorRequired,
        String email
) {
}
