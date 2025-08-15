package com.suraj.healthcare.appointment_service.dto;

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

	private LocalDate date;

	private LocalTime startTime;

	private LocalTime endTime;

	private Long doctorId;
}
