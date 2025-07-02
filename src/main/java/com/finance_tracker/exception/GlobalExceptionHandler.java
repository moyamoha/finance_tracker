package com.finance_tracker.exception;

import com.finance_tracker.exception.http.HttpException;
import com.finance_tracker.exception.http.RequestUnprocessable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationErrors(MethodArgumentNotValidException ex) {
        var validationException = new ValidationException(
                ex.getBindingResult().getFieldErrors()
        );
        return ResponseEntity.status(validationException.getStatusCode()).body(validationException.getUserReadablePayload());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleInvalidFormat(HttpMessageNotReadableException ex) {
        RequestUnprocessable exception = new RequestUnprocessable(ex.getMessage());
        return ResponseEntity.status(exception.getStatusCode()).body(exception.getUserReadablePayload());
    }

    @ExceptionHandler(HttpException.class)
    public ResponseEntity<Object> handleNotFoundException(HttpException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(ex.getUserReadablePayload());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleNotFoundException(MethodArgumentTypeMismatchException ex) {
        HttpException exception = new HttpException(HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage());
        return ResponseEntity.status(exception.getStatusCode()).body(exception.getUserReadablePayload());
    }

}
