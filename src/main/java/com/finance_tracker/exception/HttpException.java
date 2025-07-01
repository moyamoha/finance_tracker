package com.finance_tracker.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.HttpStatusCodeException;

public class HttpException extends HttpStatusCodeException implements UserReadableHttpException {

    @Getter @Setter
    protected Object detail;

    public HttpException(
            HttpStatusCode statusCode,
            String message
    ) {
        super(statusCode, message);
    }

    public HttpException(
            HttpStatusCode statusCode,
            String message,
            Object detail
    ) {
        super(statusCode, message);
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
