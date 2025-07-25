package com.finance_tracker.exception;

import com.finance_tracker._shared.HttpErrorStatus;
import com.finance_tracker.exception.http.HttpException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter @Setter
public class ValidationException extends HttpException {
    private static final String MESSAGE = "Validating request body failed";

    public ValidationException(List<FieldError> errors) {
        super(HttpErrorStatus.BAD_REQUEST, MESSAGE);
        Map<String, String> errorsMap = new HashMap<>();
        errors.forEach(error -> {
            errorsMap.put(error.getField(), error.getDefaultMessage());
        });
        this.setDetail(errorsMap);
    }
}
