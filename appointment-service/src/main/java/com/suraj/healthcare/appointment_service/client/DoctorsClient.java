package com.suraj.healthcare.appointment_service.client;

import com.suraj.healthcare.appointment_service.dto.DoctorDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "doctors-service", path = "/doctor-service/doctors")
public interface DoctorsClient {

	@GetMapping("/findById/{id}")
	DoctorDto getDoctorById(@PathVariable("id") Long id);
}
