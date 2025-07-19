package com.finance_tracker.service.validators;


public interface EntityValidator<T> {
    public void validate(T entity);
}
