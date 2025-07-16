package com.suraj.healthcare.doctor_service.service;

import com.suraj.healthcare.doctor_service.dto.AvailabilitySlotDto;

import java.time.LocalDate;
import java.util.List;

public interface AvailabilitySlotService {

	AvailabilitySlotDto createSlot(AvailabilitySlotDto dto);

	List<AvailabilitySlotDto> getSlotsByDoctorId(Long doctorId);

	List<AvailabilitySlotDto> getSlotsByDoctorIdAndDate(Long doctorId, LocalDate date);

	void deleteSlot(Long slotId);
}
