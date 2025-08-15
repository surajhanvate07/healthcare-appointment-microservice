package com.suraj.healthcare.appointment_service.repository;

import com.suraj.healthcare.appointment_service.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

	List<Appointment> findByPatientId(Long patientId);

	List<Appointment> findByDoctorId(Long doctorId);
}
