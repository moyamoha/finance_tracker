package com.finance_tracker._shared.custom_validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.ZoneId;
import java.util.Set;

public class TimezoneValidator implements ConstraintValidator<ValidTimezone, String> {

    private Set<String> availableTimezones;


    @Override
    public void initialize(ValidTimezone constraintAnnotation) {
        this.availableTimezones = ZoneId.getAvailableZoneIds();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null || s.isBlank()) return true;
        return availableTimezones.contains(s);
    }
}
