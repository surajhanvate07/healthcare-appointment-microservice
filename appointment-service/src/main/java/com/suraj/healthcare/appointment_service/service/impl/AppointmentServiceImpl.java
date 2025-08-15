package com.suraj.healthcare.appointment_service.service.impl;

import com.suraj.healthcare.appointment_service.context.UserContextHolder;
import com.suraj.healthcare.appointment_service.dto.*;
import com.suraj.healthcare.appointment_service.entity.Appointment;
import com.suraj.healthcare.appointment_service.entity.AppointmentStatus;
import com.suraj.healthcare.appointment_service.exception.AccessDeniedException;
import com.suraj.healthcare.appointment_service.exception.ResourceNotFoundException;
import com.suraj.healthcare.appointment_service.repository.AppointmentRepository;
import com.suraj.healthcare.appointment_service.service.AppointmentService;
import feign.FeignException;
import jakarta.ws.rs.ServiceUnavailableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.suraj.healthcare.appointment_service.entity.AppointmentStatus.BOOKED;
import static com.suraj.healthcare.appointment_service.entity.AppointmentStatus.RESCHEDULED;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {

	private final AppointmentRepository appointmentRepository;
	private final ModelMapper modelMapper;
	private final DoctorService doctorService;
	private final DoctorAvailabilityService doctorAvailabilityService;

	@Override
	public AppointmentResponseDto bookAppointment(AppointmentRequestDto request) {
		DoctorDto doctor;
		try {
			doctor = doctorService.getDoctorById(request.getDoctorId());
		} catch (FeignException.NotFound ex) {
			throw new ResourceNotFoundException("Doctor not found with ID: " + request.getDoctorId());
		}
		if (doctor == null) {
			throw new ResourceNotFoundException("Doctor not found with ID: " + request.getDoctorId());
		}

		List<AvailabilitySlotDto> slotDtoList;
		try {
			slotDtoList = doctorAvailabilityService.getSlotsByDoctorId(doctor.getId());
		} catch (FeignException ex) {
			log.error("Error fetching availability slots for doctor ID {}: {}", doctor.getId(), ex.getMessage());
			throw new ServiceUnavailableException("Doctor's availability service is currently unavailable");
		}

		if (slotDtoList.isEmpty()) {
			throw new ResourceNotFoundException("No availability slots found for doctor ID: " + doctor.getId());
		}

		boolean slotAvailable = slotDtoList.stream()
				.anyMatch(slot ->
						slot.getDate().equals(request.getAppointmentDateTime().toLocalDate()) &&
								!request.getAppointmentDateTime().toLocalTime().isBefore(slot.getStartTime()) &&
								request.getAppointmentDateTime().toLocalTime().isBefore(slot.getEndTime())
				);

		if (!slotAvailable) {
			throw new ResourceNotFoundException("Unable to book appointment, no available slot for doctor ID: " + doctor.getId() +
					" on " + request.getAppointmentDateTime().toLocalDate());
		}

		Appointment appointment = Appointment.builder()
				.patientId(request.getPatientId())
				.doctorId(request.getDoctorId())
				.appointmentDateTime(request.getAppointmentDateTime())
				.remarks(request.getRemarks())
				.status(BOOKED)
				.build();

		return modelMapper.map(appointmentRepository.save(appointment), AppointmentResponseDto.class);
	}

	@Override
	public AppointmentResponseDto getAppointmentById(Long id) {
		Appointment appointment = appointmentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + id));
		return modelMapper.map(appointment, AppointmentResponseDto.class);
	}

	@Override
	public List<AppointmentResponseDto> getAppointmentsByPatient(Long patientId) {
		authorizePatientOrAdmin(patientId);
		return appointmentRepository.findByPatientId(patientId).stream()
				.map(a -> modelMapper.map(a, AppointmentResponseDto.class))
				.toList();
	}

	@Override
	public List<AppointmentResponseDto> getAppointmentsByDoctor(Long doctorId) {
		return appointmentRepository.findByDoctorId(doctorId).stream()
				.map(a -> modelMapper.map(a, AppointmentResponseDto.class))
				.toList();
	}

	@Override
	public AppointmentResponseDto rescheduleAppointment(Long id, RescheduleRequestDto request) {
		Appointment appointment = appointmentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + id));
		authorizePatientOrAdmin(appointment.getPatientId());
		appointment.setAppointmentDateTime(request.getNewAppointmentDateTime());
		appointment.setStatus(RESCHEDULED);
		if (request.getRemarks() != null) {
			appointment.setRemarks(request.getRemarks());
		}
		return modelMapper.map(appointmentRepository.save(appointment), AppointmentResponseDto.class);
	}

	@Override
	public void cancelAppointment(Long id) {
		Appointment appointment = appointmentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + id));
		appointment.setStatus(AppointmentStatus.CANCELLED);
		appointmentRepository.save(appointment);
	}

	private void authorizePatientOrAdmin(Long patientId) {
		var user = UserContextHolder.getCurrentUser();
		String role = user.role();
		Long id = user.id();
		if (role == null || id == null ||
				(!role.equals("PATIENT") && !role.equals("ADMIN")) ||
				(role.equals("PATIENT") && !id.equals(patientId))) {
			throw new AccessDeniedException("Access denied, you are not authorized to access this resource");
		}
	}
}