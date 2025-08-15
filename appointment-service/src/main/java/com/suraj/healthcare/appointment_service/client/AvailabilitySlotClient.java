package com.suraj.healthcare.appointment_service.client;

import com.suraj.healthcare.appointment_service.dto.AvailabilitySlotDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "doctors-service", path = "/doctor-service/availability-slots")
public interface AvailabilitySlotClient {

	@GetMapping("/doctor/by-doctorId/{doctorId}")
	List<AvailabilitySlotDto> getSlotsByDoctor(@PathVariable Long doctorId);
}
