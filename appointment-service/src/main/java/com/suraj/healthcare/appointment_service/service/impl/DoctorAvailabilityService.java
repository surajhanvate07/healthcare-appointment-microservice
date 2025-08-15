package com.suraj.healthcare.appointment_service.service.impl;

import com.suraj.healthcare.appointment_service.client.AvailabilitySlotClient;
import com.suraj.healthcare.appointment_service.dto.AvailabilitySlotDto;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DoctorAvailabilityService {

	private final AvailabilitySlotClient availabilitySlotClient;

	@Retry(name = "availabilitySlotsService", fallbackMethod = "getSlotsByDoctorIdFallback")
	public List<AvailabilitySlotDto> getSlotsByDoctorId(Long doctorId) {
		log.info("Fetching availability slots for doctorId: {}", doctorId);
		return availabilitySlotClient.getSlotsByDoctor(doctorId);
	}

	public List<AvailabilitySlotDto> getSlotsByDoctorIdFallback(Long doctorId, Throwable ex) {
		log.warn("Fallback triggered for doctorId: {}, due to: {}", doctorId, ex.getMessage());
		return List.of(); // Return an empty list as a fallback
	}
}
