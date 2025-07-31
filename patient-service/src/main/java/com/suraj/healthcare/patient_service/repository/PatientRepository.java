package com.suraj.healthcare.patient_service.repository;

import com.suraj.healthcare.patient_service.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
	boolean existsByEmail(String email);
}
