package com.suraj.healthcare.appointment_service.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Appointment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long patientId;

	private Long doctorId;

	// The date & time selected for the appointment (from request)
	private LocalDateTime appointmentDateTime;

	// Automatically set when the booking is made
	@CreationTimestamp
	private LocalDateTime createdAt;

	@Enumerated(EnumType.STRING)
	private AppointmentStatus status;

	private String remarks;
}
