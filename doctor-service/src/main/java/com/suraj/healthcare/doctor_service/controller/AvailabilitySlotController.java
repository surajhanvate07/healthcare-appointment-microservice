package com.suraj.healthcare.doctor_service.controller;

import com.suraj.healthcare.doctor_service.dto.AvailabilitySlotDto;
import com.suraj.healthcare.doctor_service.service.AvailabilitySlotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/availability-slots")
@RequiredArgsConstructor
@Validated
public class AvailabilitySlotController {

	private final AvailabilitySlotService slotService;

	@PostMapping("/create")
	public ResponseEntity<AvailabilitySlotDto> createSlot(@Valid @RequestBody AvailabilitySlotDto dto) {
		return ResponseEntity.ok(slotService.createSlot(dto));
	}

	@GetMapping("/doctor/by-doctorId/{doctorId}")
	public ResponseEntity<List<AvailabilitySlotDto>> getSlotsByDoctor(@PathVariable Long doctorId) {
		return ResponseEntity.ok(slotService.getSlotsByDoctorId(doctorId));
	}

	@GetMapping("/doctor/by-date/{date}")
	public ResponseEntity<List<AvailabilitySlotDto>> getSlotsByDoctorAndDate(
			@PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date, @RequestParam Long doctorId) {
		return ResponseEntity.ok(slotService.getSlotsByDoctorIdAndDate(doctorId, date));
	}

	@DeleteMapping("/{slotId}")
	public ResponseEntity<Void> deleteSlot(@PathVariable Long slotId) {
		slotService.deleteSlot(slotId);
		return ResponseEntity.noContent().build();
	}
}
