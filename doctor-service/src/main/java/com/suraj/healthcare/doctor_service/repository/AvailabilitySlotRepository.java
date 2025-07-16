package com.suraj.healthcare.doctor_service.repository;

import com.suraj.healthcare.doctor_service.entity.AvailabilitySlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AvailabilitySlotRepository extends JpaRepository<AvailabilitySlot, Long> {

	List<AvailabilitySlot> findByDoctorId(Long doctorId);

	List<AvailabilitySlot> findByDoctorIdAndDate(Long doctorId, LocalDate date);
}
