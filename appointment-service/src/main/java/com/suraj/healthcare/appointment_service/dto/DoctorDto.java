package com.suraj.healthcare.appointment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoctorDto {

	private Long id;

	private String name;

	private String specialization;

	private String email;

	private String phone;
}
