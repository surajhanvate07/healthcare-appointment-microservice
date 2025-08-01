package com.suraj.healthcare.user_service.client;

import com.suraj.healthcare.user_service.dto.CreatePatientRequestDto;
import com.suraj.healthcare.user_service.dto.PatientDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "patient-service", path = "/patient-service")
public interface PatientClient {

	@PostMapping("/patients/create")
	PatientDto createPatient(@RequestBody CreatePatientRequestDto dto);
}