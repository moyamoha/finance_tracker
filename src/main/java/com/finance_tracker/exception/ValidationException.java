package com.finance_tracker.exception;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter @Setter
public class ValidationException extends HttpException {
    private static final String MESSAGE = "Validating request body failed";

    public ValidationException(List<FieldError> errors) {
        super(HttpStatus.BAD_REQUEST, MESSAGE);
        Map<String, String> errorsMap = new HashMap<>();
        errors.forEach(error -> {
            errorsMap.put(error.getField(), error.getDefaultMessage());
        });
        this.setDetail(errorsMap);
    }
}
