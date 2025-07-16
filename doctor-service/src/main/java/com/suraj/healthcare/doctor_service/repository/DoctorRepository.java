package com.suraj.healthcare.doctor_service.repository;

import com.suraj.healthcare.doctor_service.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
	boolean existsByEmail(String email);
}
