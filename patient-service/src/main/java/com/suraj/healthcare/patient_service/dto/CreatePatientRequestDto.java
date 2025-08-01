package com.suraj.healthcare.patient_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePatientRequestDto {
	@NotBlank
	@Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
	private String name;

	@NotBlank
	@Email(message = "Email should be valid")
	private String email;

	@NotBlank
	@Pattern(regexp = "^\\+?[0-9]{10,13}$", message = "Phone number must be between 10 to 13 digits and can start with '+'")
	private String phone;

	private LocalDate dob;

	private Boolean internalRequest;
}
