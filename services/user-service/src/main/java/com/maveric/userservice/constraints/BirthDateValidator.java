package com.maveric.userservice.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = BirthDateValidatorImpl.class)
public @interface BirthDateValidator {
    String message() default "{com.maveric.userservice.constraints.BirthDate.message}";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};
}
