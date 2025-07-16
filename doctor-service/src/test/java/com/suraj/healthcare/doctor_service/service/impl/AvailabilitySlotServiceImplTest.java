package com.suraj.healthcare.doctor_service.service.impl;

import com.suraj.healthcare.doctor_service.dto.AvailabilitySlotDto;
import com.suraj.healthcare.doctor_service.entity.AvailabilitySlot;
import com.suraj.healthcare.doctor_service.entity.Doctor;
import com.suraj.healthcare.doctor_service.exception.ResourceNotFoundException;
import com.suraj.healthcare.doctor_service.repository.AvailabilitySlotRepository;
import com.suraj.healthcare.doctor_service.repository.DoctorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AvailabilitySlotServiceImplTest {

	private AvailabilitySlotRepository availabilitySlotRepository;
	private DoctorRepository doctorRepository;
	private ModelMapper modelMapper;
	private AvailabilitySlotServiceImpl availabilitySlotServiceImpl;

	@BeforeEach
	public void setUp() {
		availabilitySlotRepository = mock(AvailabilitySlotRepository.class);
		doctorRepository = mock(DoctorRepository.class);
		modelMapper = new ModelMapper();
		availabilitySlotServiceImpl = new AvailabilitySlotServiceImpl(modelMapper, availabilitySlotRepository, doctorRepository);
	}

	@Test
	void createSlot_Success() {
		AvailabilitySlotDto dto = new AvailabilitySlotDto();
		dto.setDate(LocalDate.now());
		dto.setStartTime(LocalTime.parse("09:00"));
		dto.setEndTime(LocalTime.parse("10:00"));
		dto.setDoctorId(1L);

		Doctor doctor = new Doctor();
		doctor.setId(1L);
		doctor.setName("Dr. John Doe");
		doctor.setEmail(doctor.getName() + "@gmail.com");
		doctor.setSpecialization("Cardiology");
		doctor.setPhone("1234567890");

		// Mocking the doctor repository to return the doctor when queried by ID
		when(doctorRepository.findById(dto.getDoctorId())).thenReturn(java.util.Optional.of(doctor));

		AvailabilitySlot slot = AvailabilitySlot.builder()
				.date(dto.getDate())
				.startTime(dto.getStartTime())
				.endTime(dto.getEndTime())
				.doctor(doctor)
				.build();

		doctor.getAvailabilitySlots().add(slot);

		// Mocking the availability slot repository to return the saved slot
		when(availabilitySlotRepository.save(any(AvailabilitySlot.class))).thenReturn(slot);

		AvailabilitySlotDto savedSlotDto = availabilitySlotServiceImpl.createSlot(dto);

		assert savedSlotDto != null;
		assert savedSlotDto.getDate().equals(dto.getDate());
		assert savedSlotDto.getStartTime().equals(dto.getStartTime());
		assert savedSlotDto.getEndTime().equals(dto.getEndTime());
		assert savedSlotDto.getDoctorId().equals(dto.getDoctorId());
	}

	@Test
	void createSlot_NullDto_ThrowsIllegalArgumentException() {
		try {
			availabilitySlotServiceImpl.createSlot(null);
		} catch (IllegalArgumentException e) {
			assert e.getMessage().equals("AvailabilitySlotDto cannot be null");
		}
	}

	@Test
	void getSlotsByDoctorId_Success() {
		Long doctorId = 1L;
		Doctor doctor = new Doctor();
		doctor.setId(doctorId);
		doctor.setName("Dr. John Doe");
		doctor.setEmail(doctor.getName() + "@gmail.com");
		doctor.setSpecialization("Cardiology");
		doctor.setPhone("1234567890");
		AvailabilitySlot slot1 = new AvailabilitySlot();
		slot1.setId(1L);
		slot1.setDate(LocalDate.now());
		slot1.setStartTime(LocalTime.parse("09:00"));
		slot1.setEndTime(LocalTime.parse("10:00"));
		slot1.setDoctor(doctor);


		when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));
		when(availabilitySlotRepository.findByDoctorId(doctorId)).thenReturn(List.of(slot1));

		List<AvailabilitySlotDto> slots = availabilitySlotServiceImpl.getSlotsByDoctorId(doctorId);
		assert slots != null;
		assert slots.size() == 1;
		assert slots.getFirst().getId().equals(slot1.getId());
		assert slots.getFirst().getDate().equals(slot1.getDate());
		assert slots.getFirst().getStartTime().equals(slot1.getStartTime());
		assert slots.getFirst().getEndTime().equals(slot1.getEndTime());
		assert slots.getFirst().getDoctorId().equals(doctorId);
	}

	@Test
	void getSlotsByDoctorIdAndDate_Success() {
		Long doctorId = 1L;
		LocalDate date = LocalDate.now();

		Doctor doctor = new Doctor();
		doctor.setId(doctorId);
		doctor.setName("Dr. John Doe");
		doctor.setEmail(doctor.getName() + "@gmail.com");
		doctor.setSpecialization("Cardiology");
		doctor.setPhone("1234567890");

		AvailabilitySlot slot1 = new AvailabilitySlot();
		slot1.setId(1L);
		slot1.setDate(date);
		slot1.setStartTime(LocalTime.parse("09:00"));
		slot1.setEndTime(LocalTime.parse("10:00"));
		slot1.setDoctor(doctor);

		AvailabilitySlot slot2 = new AvailabilitySlot();
		slot2.setId(2L);
		slot2.setDate(LocalDate.now().plusDays(1L));
		slot2.setStartTime(LocalTime.parse("10:00"));
		slot2.setEndTime(LocalTime.parse("11:00"));
		slot2.setDoctor(doctor);

		when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));
		when(availabilitySlotRepository.findByDoctorIdAndDate(doctorId, date)).thenReturn(List.of(slot1, slot2));

		List<AvailabilitySlotDto> slots = availabilitySlotServiceImpl.getSlotsByDoctorIdAndDate(doctorId, date);
		assert slots != null;
		assert slots.size() == 2;
		assert slots.getFirst().getId().equals(slot1.getId());
		assert slots.getFirst().getDate().equals(slot1.getDate());
		assert slots.getFirst().getStartTime().equals(slot1.getStartTime());

		assert slots.get(1).getDate().equals(slot2.getDate());

	}

	@Test
	void deleteSlot_Success() {
		Long slotId = 1L;
		AvailabilitySlot slot = new AvailabilitySlot();
		slot.setId(slotId);
		slot.setDate(LocalDate.now());
		slot.setStartTime(LocalTime.parse("09:00"));
		slot.setEndTime(LocalTime.parse("10:00"));
		slot.setDoctor(new Doctor());

		when(availabilitySlotRepository.existsById(1L)).thenReturn(true);

		availabilitySlotServiceImpl.deleteSlot(slotId);

		verify(availabilitySlotRepository).deleteById(slotId);
	}

	@Test
	void deleteSlot_NotFound_ThrowsResourceNotFoundException() {
		Long slotId = 1L;
		when(availabilitySlotRepository.findById(slotId)).thenReturn(java.util.Optional.empty());

		try {
			availabilitySlotServiceImpl.deleteSlot(slotId);
		} catch (ResourceNotFoundException e) {
			assert e.getMessage().equals("Slot not found with id: " + slotId);
		}
	}
}
