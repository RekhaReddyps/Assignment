package com.maveric.userservice.constraints;

import com.maveric.userservice.constant.Gender;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class GenderValidatorImpl implements ConstraintValidator<GenderValidator, Gender> {
    private Gender[] genders;

    @Override
    public void initialize(GenderValidator constraintAnnotation) {
        this.genders = constraintAnnotation.anyOfTheseGender();
    }

    @Override
    public boolean isValid(Gender gender, ConstraintValidatorContext context) {
        return gender == null || Arrays.asList(genders).contains(gender);
    }
}
