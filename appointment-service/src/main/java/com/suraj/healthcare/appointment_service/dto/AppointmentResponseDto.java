package com.suraj.healthcare.appointment_service.dto;

import com.suraj.healthcare.appointment_service.entity.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentResponseDto {
	private Long id;
	private Long patientId;
	private Long doctorId;
	private LocalDateTime appointmentDateTime; // booked slot
	private LocalDateTime createdAt; // when appointment was booked
	private AppointmentStatus status;
	private String remarks;
}
