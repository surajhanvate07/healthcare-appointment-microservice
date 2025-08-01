package com.suraj.healthcare.patient_service.controller;

import com.suraj.healthcare.patient_service.dto.CreatePatientRequestDto;
import com.suraj.healthcare.patient_service.dto.PatientDto;
import com.suraj.healthcare.patient_service.dto.UpdatePatientDto;
import com.suraj.healthcare.patient_service.exception.AccessDeniedException;
import com.suraj.healthcare.patient_service.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {

	@Autowired
	private PatientService patientService;

	@PostMapping("/create")
	public ResponseEntity<PatientDto> create(@Valid @RequestBody CreatePatientRequestDto dto) {
		return ResponseEntity.ok(patientService.createPatient(dto));
	}

	@GetMapping("/all")
	public ResponseEntity<List<PatientDto>> getAll(@RequestHeader("X-User-Role") String requesterRole) {
		if (!requesterRole.equals("ADMIN")) {
			throw new AccessDeniedException("Access denied: Only ADMIN can view all patients");
		}
		return ResponseEntity.ok(patientService.getAllPatients());
	}

	@GetMapping("/get/{id}")
	public ResponseEntity<PatientDto> getById(@PathVariable Long id, @RequestHeader("X-User-Email") String requesterEmail,
											  @RequestHeader("X-User-Role") String requesterRole) {
		return ResponseEntity.ok(patientService.getPatientById(id, requesterEmail, requesterRole));
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<PatientDto> update(@PathVariable Long id, @Valid @RequestBody UpdatePatientDto updatePatientDto) {
		return ResponseEntity.ok(patientService.updatePatient(id, updatePatientDto));
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		patientService.deletePatient(id);
		return ResponseEntity.noContent().build();
	}
}
