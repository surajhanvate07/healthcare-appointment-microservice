package com.suraj.healthcare.doctor_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorDto {

	private Long id;

	@NotBlank(message = "Name is required")
	private String name;

	private String specialization;

	@NotBlank(message = "Email is required")
	@Email(message = "Invalid email")
	private String email;

	private String phone;

	private List<AvailabilitySlotDto> availabilitySlots;
}
