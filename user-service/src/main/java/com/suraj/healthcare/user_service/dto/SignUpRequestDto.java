package com.suraj.healthcare.user_service.dto;

import com.suraj.healthcare.user_service.annotations.PasswordStrength;
import com.suraj.healthcare.user_service.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpRequestDto {
	@NotBlank(message = "Name is required")
	private String name;

	@NotBlank(message = "Email is required")
	@Email(message = "Email must be valid")
	private String email;

	@NotBlank(message = "Password is required")
	@PasswordStrength
	private String password;

	@NotBlank(message = "Phone number is required")
	private String phone;

	private LocalDate dob;

	@NotNull(message = "Role is required")
	private Role role;
}
