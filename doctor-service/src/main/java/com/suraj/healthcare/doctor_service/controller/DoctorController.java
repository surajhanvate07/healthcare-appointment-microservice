package com.suraj.healthcare.doctor_service.controller;

import com.suraj.healthcare.doctor_service.dto.DoctorDto;
import com.suraj.healthcare.doctor_service.dto.UpdateDoctorDto;
import com.suraj.healthcare.doctor_service.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
@Validated
public class DoctorController {

	private final DoctorService doctorService;

	@PostMapping("/create-doctor")
	public ResponseEntity<DoctorDto> createDoctor(@Valid @RequestBody DoctorDto doctorDto) {
		DoctorDto createdDoctor = doctorService.createDoctor(doctorDto);
		return ResponseEntity.ok(createdDoctor);
	}

	@GetMapping("/findAll")
	public ResponseEntity<List<DoctorDto>> getAllDoctors() {
		List<DoctorDto> doctors = doctorService.getAllDoctors();
		return ResponseEntity.ok(doctors);
	}

	@GetMapping("/findById/{id}")
	public ResponseEntity<DoctorDto> getDoctorById(@PathVariable("id") Long id) {
		DoctorDto doctor = doctorService.getDoctorById(id);
		if (doctor != null) {
			return ResponseEntity.ok(doctor);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PutMapping("/update-doctor/{id}")
	public ResponseEntity<DoctorDto> updateDoctor(@PathVariable("id") Long id, @Valid @RequestBody UpdateDoctorDto updateDoctorDto) {
		DoctorDto updatedDoctor = doctorService.updateDoctor(id, updateDoctorDto);

		if (updatedDoctor != null) {
			return ResponseEntity.ok(updatedDoctor);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("/delete-doctor/{id}")
	public ResponseEntity<Void> deleteDoctor(@PathVariable("id") Long id) {
		doctorService.deleteDoctor(id);
		return ResponseEntity.noContent().build();
	}
}
