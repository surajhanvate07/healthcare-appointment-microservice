package com.suraj.healthcare.user_service.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordStrengthValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordStrength {

	String message() default "Password does not meet strength requirements";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	int minLength() default 8;
	int maxLength() default 50;
	boolean requireUppercase() default true;
	boolean requireLowercase() default true;
	boolean requireDigit() default true;
	boolean requireSpecialCharacter() default true;
}
