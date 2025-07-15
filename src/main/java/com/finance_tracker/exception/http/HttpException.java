package com.finance_tracker.exception.http;

import com.finance_tracker._shared.HttpErrorStatus;
import com.finance_tracker.exception.ApiError;
import com.finance_tracker.exception.UserReadableHttpException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpStatusCodeException;

public class HttpException extends HttpStatusCodeException implements UserReadableHttpException {

    @Getter @Setter
    protected Object detail;

    public HttpException(HttpErrorStatus status, Object detail) {
        super(status.getStatusCode(), status.getStatusText());
        this.setDetail(detail);
    }

    public HttpException(HttpStatus statusCode, String staticText, Object detail) {
        super(statusCode, staticText);
        this.setDetail(detail);
    }

    public ApiError getUserReadablePayload() {
        return new ApiError(
                this.getStatusCode().value(),
                this.getDetail(),
                this.getMessage()
        );
    }
}
