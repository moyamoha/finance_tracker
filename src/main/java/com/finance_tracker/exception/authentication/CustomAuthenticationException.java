package com.finance_tracker.exception.authentication;

import com.finance_tracker.exception.ApiError;
import com.finance_tracker.exception.UserReadableHttpException;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;

@Getter
public class CustomAuthenticationException extends AuthenticationException implements UserReadableHttpException {
    private final HttpStatus status;
    private final String statusText = "Not authenticated";

    public CustomAuthenticationException(String detail) {
        super(detail);
        this.status = HttpStatus.UNAUTHORIZED;
    }

    @Override
    public ApiError getUserReadablePayload() {
        return new ApiError(
            this.getStatus().value(),
            this.getMessage(),
            this.getStatusText()
        );
    }
}
