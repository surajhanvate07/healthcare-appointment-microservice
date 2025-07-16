package com.suraj.healthcare.doctor_service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AvailabilitySlotDto {

	private Long id;

	@NotNull(message = "Date is required")
	private LocalDate date;

	@NotNull(message = "Start time is required")
	private LocalTime startTime;

	@NotNull(message = "End time is required")
	private LocalTime endTime;

	@NotNull(message = "Doctor ID is required")
	private Long doctorId;
}
