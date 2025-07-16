package com.suraj.healthcare.doctor_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UpdateDoctorDto {
	private Long id;

	private String name;

	private String specialization;

	private String email;

	private String phone;
}
