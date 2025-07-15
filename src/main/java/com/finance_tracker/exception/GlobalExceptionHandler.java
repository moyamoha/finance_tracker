package com.finance_tracker.exception;

import com.finance_tracker._shared.HttpErrorStatus;
import com.finance_tracker.exception.authentication.CustomAuthenticationException;
import com.finance_tracker.exception.custom.CustomApplicationException;
import com.finance_tracker.exception.http.BadRequestException;
import com.finance_tracker.exception.http.HttpException;
import com.finance_tracker.exception.http.RequestUnprocessable;
import com.finance_tracker.exception.http.UnprocessableEntityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

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
        UnprocessableEntityException exception = new UnprocessableEntityException(ex.getMessage());
        return ResponseEntity.status(exception.getStatusCode()).body(exception.getUserReadablePayload());
    }

    @ExceptionHandler(HttpException.class)
    public ResponseEntity<Object> handleHttpException(HttpException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(ex.getUserReadablePayload());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleNotFoundException(MethodArgumentTypeMismatchException ex) {
        HttpException exception = new BadRequestException(ex.getMessage());
        return ResponseEntity.status(exception.getStatusCode()).body(exception.getUserReadablePayload());
    }

    @ExceptionHandler(CustomApplicationException.class)
    public ResponseEntity<Object> handleCustomException(CustomApplicationException ex) {
        HttpException exception = new UnprocessableEntityException(ex.getMessage());
        return ResponseEntity.status(exception.getStatusCode()).body(exception.getUserReadablePayload());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleIntegrityViolation(DataIntegrityViolationException ex) {
        if (ex instanceof DuplicateKeyException) {
            String message = "Duplicate key for " + ex.getMessage();
            HttpException exception = new HttpException(HttpStatus.UNPROCESSABLE_ENTITY, "Content is unprocessable", message);
            return ResponseEntity.status(exception.getStatusCode()).body(exception.getUserReadablePayload());
        } else {
            HttpException exception = new HttpException(HttpStatus.UNPROCESSABLE_ENTITY, "Content is unprocessable", ex.getMessage());
            return ResponseEntity.status(exception.getStatusCode()).body(exception.getUserReadablePayload());
        }
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Object> handleRouteNotFound(NoHandlerFoundException ex) {
        HttpException exception = new HttpException(HttpErrorStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(exception.getStatusCode()).body(exception.getUserReadablePayload());
    }

}
