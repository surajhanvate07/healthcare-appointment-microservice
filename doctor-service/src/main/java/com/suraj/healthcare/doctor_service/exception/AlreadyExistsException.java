package com.suraj.healthcare.doctor_service.exception;

public class AlreadyExistsException extends RuntimeException {
	public AlreadyExistsException(String message) {
		super(message);
	}
}
