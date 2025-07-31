package com.suraj.healthcare.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientDto {

	private Long id;
	private String name;
	private String email;
	private String phone;
	private LocalDate dob;
}
