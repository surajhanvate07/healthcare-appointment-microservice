package com.suraj.healthcare.user_service.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class PasswordStrengthValidator implements ConstraintValidator<PasswordStrength, String> {

	private static final String UPPERCASE_PATTERN = ".*[A-Z].*";
	private static final String LOWERCASE_PATTERN = ".*[a-z].*";
	private static final String DIGIT_PATTERN = ".*[0-9].*";
	private static final String SPECIAL_CHARACTER_PATTERN = ".*[!@#$%^&*(),.?\":{}|<>].*";
	private int minLength;
	private int maxLength;
	private boolean requireUppercase;
	private boolean requireLowercase;
	private boolean requireDigit;
	private boolean requireSpecialCharacter;

	@Override
	public void initialize(PasswordStrength constraintAnnotation) {
		this.minLength = constraintAnnotation.minLength();
		this.maxLength = constraintAnnotation.maxLength();
		this.requireUppercase = constraintAnnotation.requireUppercase();
		this.requireLowercase = constraintAnnotation.requireLowercase();
		this.requireDigit = constraintAnnotation.requireDigit();
		this.requireSpecialCharacter = constraintAnnotation.requireSpecialCharacter();

	}

	@Override
	public boolean isValid(String password, ConstraintValidatorContext context) {
		if (password == null) {
			return false;
		}
		context.disableDefaultConstraintViolation();

		boolean isValid = true;

		if (password.length() < minLength || password.length() > maxLength) {
			context.buildConstraintViolationWithTemplate("Password must be between " + minLength + " and " + maxLength + " characters long")
					.addConstraintViolation();
			isValid = false;
		}

		if (requireUppercase && !Pattern.matches(UPPERCASE_PATTERN, password)) {
			context.buildConstraintViolationWithTemplate("Password must contain at least one uppercase letter")
					.addConstraintViolation();
			isValid = false;
		}

		if (requireLowercase && !password.matches(LOWERCASE_PATTERN)) {
			context.buildConstraintViolationWithTemplate("Password must contain at least one lowercase letter")
					.addConstraintViolation();
			isValid = false;
		}

		if (requireDigit && !password.matches(DIGIT_PATTERN)) {
			context.buildConstraintViolationWithTemplate("Password must contain at least one digit")
					.addConstraintViolation();
			isValid = false;
		}

		if (requireSpecialCharacter && !password.matches(SPECIAL_CHARACTER_PATTERN)) {
			context.buildConstraintViolationWithTemplate("Password must contain at least one special character")
					.addConstraintViolation();
			isValid = false;
		}

		return isValid;
	}
}
