package com.suraj.healthcare.patient_service.controller;

import com.suraj.healthcare.patient_service.context.UserContextHolder;
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
		// If not an internal request, enforce role check
		if(!Boolean.TRUE.equals(dto.getInternalRequest())) {
			var user = UserContextHolder.getCurrentUser();

			if (user == null) {
				throw new AccessDeniedException("Access denied: Missing user context");
			}
			String requesterRole = user.role();

			if (!requesterRole.equals("ADMIN") && !requesterRole.equals("DOCTOR")) {
				throw new AccessDeniedException("Access denied: Only ADMIN / DOCTOR can create patients");
			}
		}
		return ResponseEntity.ok(patientService.createPatient(dto));
	}

	@GetMapping("/all")
	public ResponseEntity<List<PatientDto>> getAll() {
		String requesterRole = UserContextHolder.getCurrentUser().role();

		if (!requesterRole.equals("ADMIN")) {
			throw new AccessDeniedException("Access denied: Only ADMIN can view all patients");
		}
		return ResponseEntity.ok(patientService.getAllPatients());
	}

	@GetMapping("/get/{id}")
	public ResponseEntity<PatientDto> getById(@PathVariable Long id) {
		return ResponseEntity.ok(patientService.getPatientById(id));
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
