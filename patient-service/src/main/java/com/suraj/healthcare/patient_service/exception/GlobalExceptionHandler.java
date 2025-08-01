package com.suraj.healthcare.patient_service.exception;

import com.suraj.healthcare.patient_service.response.ApiExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiExceptionResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
		ApiExceptionResponse response = ApiExceptionResponse.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.NOT_FOUND.value())
				.error(HttpStatus.NOT_FOUND.getReasonPhrase())
				.message(ex.getLocalizedMessage())
				.build();

		return ResponseEntity.status(404).body(response);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ApiExceptionResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
		ApiExceptionResponse response = ApiExceptionResponse.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.BAD_REQUEST.value())
				.error(HttpStatus.BAD_REQUEST.getReasonPhrase())
				.message(ex.getLocalizedMessage())
				.build();
		return ResponseEntity.badRequest().body(response);
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ApiExceptionResponse> handleAccessDeniedException(AccessDeniedException ex) {
		ApiExceptionResponse response = ApiExceptionResponse.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.FORBIDDEN.value())
				.error(HttpStatus.FORBIDDEN.getReasonPhrase())
				.message(ex.getLocalizedMessage())
				.build();
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(error ->
				errors.put(error.getField(), error.getDefaultMessage()));
		return ResponseEntity.badRequest().body(errors);
	}
}
