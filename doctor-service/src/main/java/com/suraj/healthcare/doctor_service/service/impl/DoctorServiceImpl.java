package com.suraj.healthcare.doctor_service.service.impl;

import com.suraj.healthcare.doctor_service.dto.AvailabilitySlotDto;
import com.suraj.healthcare.doctor_service.dto.DoctorDto;
import com.suraj.healthcare.doctor_service.dto.UpdateDoctorDto;
import com.suraj.healthcare.doctor_service.entity.Doctor;
import com.suraj.healthcare.doctor_service.exception.AlreadyExistsException;
import com.suraj.healthcare.doctor_service.exception.ResourceNotFoundException;
import com.suraj.healthcare.doctor_service.repository.DoctorRepository;
import com.suraj.healthcare.doctor_service.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

	private final DoctorRepository doctorRepository;
	private final ModelMapper modelMapper;

	@Override
	public DoctorDto createDoctor(DoctorDto doctorDto) {
		// check if a doctorDto is null
		if (doctorDto == null) {
			throw new IllegalArgumentException("DoctorDto cannot be null");
		}

		// Convert DoctorDto to Doctor entity
		Doctor doctor = modelMapper.map(doctorDto, Doctor.class);

		// check if a doctor with the same email already exists
		if (isDoctorAlreadyExists(doctor.getEmail())) {
			throw new AlreadyExistsException("Doctor with email " + doctor.getEmail() + " already exists");
		}
		Doctor savedDoctor = doctorRepository.save(doctor);
		// Convert saved Doctor entity back to DoctorDto
		return modelMapper.map(savedDoctor, DoctorDto.class);
	}

	@Override
	public DoctorDto getDoctorById(Long id) {
		if (id == null) {
			throw new IllegalArgumentException("Doctor ID cannot be null");
		}
		Doctor doctor = doctorRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + id));

		DoctorDto doctorDto = modelMapper.map(doctor, DoctorDto.class);

		List<AvailabilitySlotDto> slotDtos = doctor.getAvailabilitySlots().stream()
				.map(slot -> modelMapper.map(slot, AvailabilitySlotDto.class))
				.toList();

		doctorDto.setAvailabilitySlots(slotDtos);
		return doctorDto;
	}

	@Override
	public List<DoctorDto> getAllDoctors() {
		return doctorRepository.findAll().stream().map(doctor -> {
			DoctorDto dto = modelMapper.map(doctor, DoctorDto.class);

			List<AvailabilitySlotDto> slotDtos = doctor.getAvailabilitySlots().stream()
					.map(slot -> modelMapper.map(slot, AvailabilitySlotDto.class))
					.collect(Collectors.toList());

			dto.setAvailabilitySlots(slotDtos);
			return dto;
		}).collect(Collectors.toList());
	}

	@Override
	public DoctorDto updateDoctor(Long id, UpdateDoctorDto updateDoctorDto) {
		if (updateDoctorDto == null) {
			throw new IllegalArgumentException("DoctorDto cannot be null");
		}
		Doctor existingDoctor = doctorRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + id));

		// Update the existing doctor's fields with the new values
		if (updateDoctorDto.getName() != null && !updateDoctorDto.getName().isBlank()) {
			existingDoctor.setName(updateDoctorDto.getName());
		}
		// If specialization is provided, update it
		if (updateDoctorDto.getSpecialization() != null && !updateDoctorDto.getSpecialization().isBlank()) {
			existingDoctor.setSpecialization(updateDoctorDto.getSpecialization());
		}
		// Update email
		if (updateDoctorDto.getEmail() != null && !updateDoctorDto.getEmail().isBlank()) {
			if (isDoctorAlreadyExists(updateDoctorDto.getEmail())) {
				throw new AlreadyExistsException("This email " + updateDoctorDto.getEmail() + " already has been used by another doctor");
			}
			existingDoctor.setEmail(updateDoctorDto.getEmail());
		}
		// Update phone if provided
		if (updateDoctorDto.getPhone() != null && !updateDoctorDto.getPhone().isBlank()) {
			existingDoctor.setPhone(updateDoctorDto.getPhone());
		}

		// Save the updated doctor entity
		Doctor updatedDoctor = doctorRepository.save(existingDoctor);
		return modelMapper.map(updatedDoctor, DoctorDto.class);
	}

	@Override
	@CacheEvict(value = "availabilitySlots", key = "#id")
	public void deleteDoctor(Long id) {
		Doctor doctor = doctorRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + id));
		doctorRepository.delete(doctor);
	}

	private boolean isDoctorAlreadyExists(String email) {
		return doctorRepository.existsByEmail(email);
	}

}
