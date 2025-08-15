package com.suraj.healthcare.appointment_service.controller;


import com.suraj.healthcare.appointment_service.dto.AppointmentRequestDto;
import com.suraj.healthcare.appointment_service.dto.AppointmentResponseDto;
import com.suraj.healthcare.appointment_service.dto.RescheduleRequestDto;
import com.suraj.healthcare.appointment_service.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

	private final AppointmentService appointmentService;

	// Book an appointment
	@PostMapping("/book")
	public ResponseEntity<AppointmentResponseDto> bookAppointment(@RequestBody AppointmentRequestDto request) {
		return ResponseEntity.ok(appointmentService.bookAppointment(request));
	}

	// Get appointment by ID
	@GetMapping("/{id}")
	public ResponseEntity<AppointmentResponseDto> getAppointment(@PathVariable Long id) {
		return ResponseEntity.ok(appointmentService.getAppointmentById(id));
	}

	// Get all appointments for a patient
	@GetMapping("/patient/{patientId}")
	public ResponseEntity<List<AppointmentResponseDto>> getAppointmentsByPatient(@PathVariable Long patientId) {
		return ResponseEntity.ok(appointmentService.getAppointmentsByPatient(patientId));
	}

	// Get all appointments for a doctor
	@GetMapping("/doctor/{doctorId}")
	public ResponseEntity<List<AppointmentResponseDto>> getAppointmentsByDoctor(@PathVariable Long doctorId) {
		return ResponseEntity.ok(appointmentService.getAppointmentsByDoctor(doctorId));
	}

	// Reschedule appointment
	@PutMapping("/patient/{id}/reschedule")
	public ResponseEntity<AppointmentResponseDto> rescheduleAppointment(
			@PathVariable Long id,
			@RequestBody RescheduleRequestDto request) {
		return ResponseEntity.ok(appointmentService.rescheduleAppointment(id, request));
	}

	// Cancel appointment
	@DeleteMapping("/patient/{id}")
	public ResponseEntity<Void> cancelAppointment(@PathVariable Long id) {
		appointmentService.cancelAppointment(id);
		return ResponseEntity.noContent().build();
	}
}
