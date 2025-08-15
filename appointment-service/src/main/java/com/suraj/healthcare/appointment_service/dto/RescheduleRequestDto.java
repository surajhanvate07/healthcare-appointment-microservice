package com.suraj.healthcare.appointment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RescheduleRequestDto {

	private LocalDateTime newAppointmentDateTime;
	private String remarks;
}
