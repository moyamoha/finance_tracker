package com.finance_tracker._shared.custom_validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Currency;

public class CurrencyValidator implements ConstraintValidator<ValidCurrency, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null || s.isBlank()) return true;
        try {
            Currency.getInstance(s);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
