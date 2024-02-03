package com.maveric.userservice.constraints;

import com.maveric.userservice.constant.Gender;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = GenderValidatorImpl.class)
public @interface GenderValidator {

    Gender[] anyOfTheseGender();
    String message() default "Gender should be either {anyOfTheseGender}";
    Class<?> [] groups() default {};
    Class<? extends Payload> [] payload() default {};
}
