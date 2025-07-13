package com.suraj.healthcare.user_service.exception;

import com.suraj.healthcare.user_service.response.ApiExceptionResponse;
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

	@ExceptionHandler(AlreadyExistsException.class)
	public ResponseEntity<ApiExceptionResponse> handleAlreadyExistsException(AlreadyExistsException ex) {
		ApiExceptionResponse response = ApiExceptionResponse.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.CONFLICT.value())
				.error(HttpStatus.CONFLICT.getReasonPhrase())
				.message(ex.getLocalizedMessage())
				.build();
		return new ResponseEntity<>(response, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ApiExceptionResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
		ApiExceptionResponse response = ApiExceptionResponse.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.BAD_REQUEST.value())
				.error(HttpStatus.BAD_REQUEST.getReasonPhrase())
				.message(ex.getLocalizedMessage())
				.build();
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiExceptionResponse> handleGenericException(Exception ex) {
		ApiExceptionResponse response = ApiExceptionResponse.builder()
				.timestamp(LocalDateTime.now())
				.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
				.message(ex.getLocalizedMessage())
				.build();
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(error ->
				errors.put(error.getField(), error.getDefaultMessage()));
		return ResponseEntity.badRequest().body(errors);
	}
}
