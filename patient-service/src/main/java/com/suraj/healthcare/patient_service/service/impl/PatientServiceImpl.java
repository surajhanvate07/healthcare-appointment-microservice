package com.suraj.healthcare.patient_service.service.impl;

import com.suraj.healthcare.patient_service.context.UserContextHolder;
import com.suraj.healthcare.patient_service.dto.CreatePatientRequestDto;
import com.suraj.healthcare.patient_service.dto.PatientDto;
import com.suraj.healthcare.patient_service.dto.UpdatePatientDto;
import com.suraj.healthcare.patient_service.entity.Patient;
import com.suraj.healthcare.patient_service.exception.AccessDeniedException;
import com.suraj.healthcare.patient_service.exception.AlreadyExistsException;
import com.suraj.healthcare.patient_service.exception.ResourceNotFoundException;
import com.suraj.healthcare.patient_service.repository.PatientRepository;
import com.suraj.healthcare.patient_service.service.PatientService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientServiceImpl implements PatientService {

	private final ModelMapper modelMapper;
	private final PatientRepository patientRepository;

	public PatientServiceImpl(ModelMapper modelMapper, PatientRepository patientRepository) {
		this.modelMapper = modelMapper;
		this.patientRepository = patientRepository;
	}

	@Override
	public PatientDto createPatient(CreatePatientRequestDto createPatientRequestDto) {
		if (createPatientRequestDto == null) {
			throw new IllegalArgumentException("CreatePatientRequestDto cannot be null");
		}

		Patient patient = modelMapper.map(createPatientRequestDto, Patient.class);

		if (isPatientAlreadyExists(patient.getEmail())) {
			throw new AlreadyExistsException("Patient with email " + patient.getEmail() + " already exists");
		}

		Patient savedPatient = patientRepository.save(patient);

		return modelMapper.map(savedPatient, PatientDto.class);
	}

	@Override
	public List<PatientDto> getAllPatients() {
		return patientRepository.findAll().stream()
				.map(patient -> modelMapper.map(patient, PatientDto.class))
				.toList();
	}

	@Override
	public PatientDto getPatientById(Long id) {
		if (id == null) {
			throw new IllegalArgumentException("Patient ID cannot be null");
		}

		Patient patient = patientRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));

		checkRoleAccess(patient);

		return modelMapper.map(patient, PatientDto.class);
	}

	@Override
	public PatientDto updatePatient(Long id, UpdatePatientDto updatePatientRequestDto) {
		if (id == null || updatePatientRequestDto == null) {
			throw new IllegalArgumentException("Patient ID and UpdatePatientDto cannot be null");
		}
		Patient existingPatient = patientRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));

		checkRoleAccess(existingPatient);

		if (updatePatientRequestDto.getName() != null && !updatePatientRequestDto.getName().isBlank()) {
			existingPatient.setName(updatePatientRequestDto.getName());
		}
		if (updatePatientRequestDto.getAddress() != null && !updatePatientRequestDto.getAddress().isBlank()) {
			existingPatient.setAddress(updatePatientRequestDto.getAddress());
		}
		if (updatePatientRequestDto.getDob() != null && !updatePatientRequestDto.getDob().isAfter(java.time.LocalDate.now())) {
			existingPatient.setDob(updatePatientRequestDto.getDob());
		}

		Patient savedPatient = patientRepository.save(existingPatient);
		return modelMapper.map(savedPatient, PatientDto.class);
	}

	@Override
	public void deletePatient(Long id) {
		if (id == null) {
			throw new IllegalArgumentException("Patient ID cannot be null");
		}

		Patient existingPatient = patientRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + id));

		checkRoleAccess(existingPatient);

		patientRepository.deleteById(id);

	}

	private boolean isPatientAlreadyExists(String email) {
		return patientRepository.existsByEmail(email);
	}

	private void checkRoleAccess(Patient existingPatient) {
		// Get the current user's role and email from UserContextHolder
		String requesterRole = UserContextHolder.getCurrentUser().role();
		String requesterEmail = UserContextHolder.getCurrentUser().email();

		if (requesterRole != null && requesterRole.equals("PATIENT") && !existingPatient.getEmail().equals(requesterEmail)) {
			throw new AccessDeniedException("Access denied: You can only update your own patient details");
		}
	}
}
