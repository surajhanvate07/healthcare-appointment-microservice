package com.suraj.healthcare.patient_service.service;

import com.suraj.healthcare.patient_service.dto.CreatePatientRequestDto;
import com.suraj.healthcare.patient_service.dto.PatientDto;

import java.util.List;

public interface PatientService {

	PatientDto createPatient(CreatePatientRequestDto createPatientRequestDto);

	List<PatientDto> getAllPatients();

	PatientDto getPatientById(Long id);

	void deletePatient(Long id);
}
