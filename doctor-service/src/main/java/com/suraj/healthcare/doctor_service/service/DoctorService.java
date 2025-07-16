package com.suraj.healthcare.doctor_service.service;

import com.suraj.healthcare.doctor_service.dto.DoctorDto;
import com.suraj.healthcare.doctor_service.dto.UpdateDoctorDto;

import java.util.List;

public interface DoctorService {
	DoctorDto createDoctor(DoctorDto doctorDto);

	DoctorDto getDoctorById(Long id);

	List<DoctorDto> getAllDoctors();

	DoctorDto updateDoctor(Long id, UpdateDoctorDto updateDoctorDto);

	void deleteDoctor(Long id);
}
