package com.suraj.healthcare.appointment_service.service.impl;

import com.suraj.healthcare.appointment_service.client.DoctorsClient;
import com.suraj.healthcare.appointment_service.dto.DoctorDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DoctorService {

	private final DoctorsClient doctorsClient;

	@CircuitBreaker(name = "doctorsService", fallbackMethod = "getDoctorFallback")
//	@Retry(name = "doctorsService" , fallbackMethod = "getDoctorFallback")
	public DoctorDto getDoctorById(Long doctorId) {
		return doctorsClient.getDoctorById(doctorId);
	}

	// Fallback method must match the original method’s signature + Throwable
	public DoctorDto getDoctorFallback(Long doctorId, Throwable ex) {
		log.warn("Fallback triggered for doctorId: {}, due to: {}", doctorId, ex.getMessage());
		DoctorDto fallback = new DoctorDto();
		fallback.setId(doctorId);
		fallback.setName("Unavailable");
		fallback.setSpecialization("Unknown");
		return fallback;
	}
}
