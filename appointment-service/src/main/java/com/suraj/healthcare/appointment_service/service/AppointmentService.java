package com.suraj.healthcare.appointment_service.service;

import com.suraj.healthcare.appointment_service.dto.AppointmentRequestDto;
import com.suraj.healthcare.appointment_service.dto.AppointmentResponseDto;
import com.suraj.healthcare.appointment_service.dto.RescheduleRequestDto;

import java.util.List;

public interface AppointmentService {

	AppointmentResponseDto bookAppointment(AppointmentRequestDto request);

	AppointmentResponseDto getAppointmentById(Long id);

	List<AppointmentResponseDto> getAppointmentsByPatient(Long patientId);

	List<AppointmentResponseDto> getAppointmentsByDoctor(Long doctorId);

	AppointmentResponseDto rescheduleAppointment(Long id, RescheduleRequestDto request);

	void cancelAppointment(Long id);

}
