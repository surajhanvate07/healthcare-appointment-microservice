package com.suraj.healthcare.patient_service.service;

import com.suraj.healthcare.patient_service.dto.CreatePatientRequestDto;
import com.suraj.healthcare.patient_service.dto.PatientDto;
import com.suraj.healthcare.patient_service.dto.UpdatePatientDto;

import java.util.List;

public interface PatientService {

	PatientDto createPatient(CreatePatientRequestDto createPatientRequestDto);

	List<PatientDto> getAllPatients();

	PatientDto getPatientById(Long id, String requesterEmail, String requesterRole);

	PatientDto updatePatient(Long id, UpdatePatientDto updatePatientRequestDto);

	void deletePatient(Long id);
}
