package com.maveric.userservice.constraints;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Calendar;
import java.util.Date;

public class BirthDateValidatorImpl implements ConstraintValidator<BirthDateValidator, Date> {
    @Override
    public boolean isValid(final Date valueToValidate, final ConstraintValidatorContext context) {
        Calendar dateInCalander = Calendar.getInstance();
        dateInCalander.setTime(valueToValidate);

        return Calendar.getInstance().get(Calendar.YEAR) - dateInCalander.get(Calendar.YEAR) >= 18;
    }
}
