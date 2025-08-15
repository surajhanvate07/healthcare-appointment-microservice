package com.suraj.healthcare.appointment_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentRequestDto {

	@NotBlank(message = "Patient ID cannot be null")
	private Long patientId;

	@NotBlank(message = "Doctor ID cannot be null")
	private Long doctorId;

	@NotBlank(message = "Appointment date and time cannot be null")
	@Future(message = "Appointment date and time must be in the future")
	@JsonFormat(pattern = "dd-MM-yyyy HH:mm")
	private LocalDateTime appointmentDateTime;

	private String remarks;
}
