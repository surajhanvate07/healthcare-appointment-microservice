package com.suraj.healthcare.user_service.exception;

public class UserNotExistsException extends RuntimeException {
	public UserNotExistsException(String message) {
		super(message);
	}
}
