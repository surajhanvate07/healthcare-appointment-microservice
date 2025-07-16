package com.suraj.healthcare.doctor_service.service.impl;

import com.suraj.healthcare.doctor_service.dto.AvailabilitySlotDto;
import com.suraj.healthcare.doctor_service.entity.AvailabilitySlot;
import com.suraj.healthcare.doctor_service.entity.Doctor;
import com.suraj.healthcare.doctor_service.exception.ResourceNotFoundException;
import com.suraj.healthcare.doctor_service.repository.AvailabilitySlotRepository;
import com.suraj.healthcare.doctor_service.repository.DoctorRepository;
import com.suraj.healthcare.doctor_service.service.AvailabilitySlotService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvailabilitySlotServiceImpl implements AvailabilitySlotService {

	private final ModelMapper modelMapper;
	private final AvailabilitySlotRepository availabilitySlotRepository;
	private final DoctorRepository doctorRepository;

	@Override
	public AvailabilitySlotDto createSlot(AvailabilitySlotDto dto) {
		if (dto == null) {
			throw new IllegalArgumentException("AvailabilitySlotDto cannot be null");
		}

		Doctor doctor = getDoctorById(dto.getDoctorId());

		AvailabilitySlot slot = AvailabilitySlot.builder()
				.date(dto.getDate())
				.startTime(dto.getStartTime())
				.endTime(dto.getEndTime())
				.doctor(doctor)
				.build();

		doctor.getAvailabilitySlots().add(slot);

		AvailabilitySlot savedSlot = availabilitySlotRepository.save(slot);
		return modelMapper.map(savedSlot, AvailabilitySlotDto.class);
	}

	@Override
	public List<AvailabilitySlotDto> getSlotsByDoctorId(Long doctorId) {
		Doctor doctor = getDoctorById(doctorId);

		return availabilitySlotRepository.findByDoctorId(doctor.getId()).stream()
				.map(slot -> modelMapper.map(slot, AvailabilitySlotDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public List<AvailabilitySlotDto> getSlotsByDoctorIdAndDate(Long doctorId, LocalDate date) {
		Doctor doctor = getDoctorById(doctorId);

		List<AvailabilitySlot> availabilitySlots = availabilitySlotRepository.findByDoctorIdAndDate(doctorId, date);

		return availabilitySlots.stream()
				.map(slot -> modelMapper.map(slot, AvailabilitySlotDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public void deleteSlot(Long slotId) {
		if (!availabilitySlotRepository.existsById(slotId)) {
			throw new ResourceNotFoundException("Slot not found with id: " + slotId);
		}
		availabilitySlotRepository.deleteById(slotId);
	}

	private Doctor getDoctorById(Long doctorId) {
		return doctorRepository.findById(doctorId)
				.orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + doctorId));
	}
}
