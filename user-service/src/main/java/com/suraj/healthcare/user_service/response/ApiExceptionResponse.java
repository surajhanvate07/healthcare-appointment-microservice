package com.suraj.healthcare.user_service.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiExceptionResponse {
	private LocalDateTime timestamp;
	private int status;
	private String error;
	private String message;
}
