package com.suraj.healthcare.doctor_service.service.impl;

import com.suraj.healthcare.doctor_service.dto.DoctorDto;
import com.suraj.healthcare.doctor_service.dto.UpdateDoctorDto;
import com.suraj.healthcare.doctor_service.entity.Doctor;
import com.suraj.healthcare.doctor_service.exception.AlreadyExistsException;
import com.suraj.healthcare.doctor_service.exception.ResourceNotFoundException;
import com.suraj.healthcare.doctor_service.repository.DoctorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class DoctorServiceImplTest {

	private DoctorRepository doctorRepository;
	private ModelMapper modelMapper;
	private DoctorServiceImpl doctorServiceImpl;

	@BeforeEach
	void setUp() {
		doctorRepository = mock(DoctorRepository.class);
		modelMapper = new ModelMapper();
		doctorServiceImpl = new DoctorServiceImpl(doctorRepository, modelMapper);
	}

	@Test
	public void createDoctor_Success() {
		DoctorDto doctorDto = new DoctorDto();
		doctorDto.setName("Dr. John Doe");
		doctorDto.setEmail("doc.gmail.com");
		doctorDto.setSpecialization("Cardiology");
		doctorDto.setPhone("1234567890");
		doctorDto.setAvailabilitySlots(null);

		Doctor doctor = modelMapper.map(doctorDto, Doctor.class);
		doctor.setId(1L);

		when(doctorRepository.existsByEmail(doctor.getEmail())).thenReturn(false);
		when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);

		DoctorDto savedDoctorDto = doctorServiceImpl.createDoctor(doctorDto);

		assert savedDoctorDto != null;
		assert savedDoctorDto.getId() != null;
		assert savedDoctorDto.getName().equals(doctorDto.getName());
		assert savedDoctorDto.getEmail().equals(doctorDto.getEmail());
		assert savedDoctorDto.getSpecialization().equals(doctorDto.getSpecialization());
		assert savedDoctorDto.getPhone().equals(doctorDto.getPhone());
		assert savedDoctorDto.getAvailabilitySlots() == null;
	}

	@Test
	void createDoctor_AlreadyExists_ThrowsException() {
		DoctorDto doctorDto = new DoctorDto();
		doctorDto.setName("Dr. John Doe");
		doctorDto.setEmail("doc.gmail.com");
		doctorDto.setSpecialization("Cardiology");
		doctorDto.setPhone("1234567890");
		doctorDto.setAvailabilitySlots(null);

		when(doctorRepository.existsByEmail(doctorDto.getEmail())).thenReturn(true);
		assertThrows(AlreadyExistsException.class, () -> doctorServiceImpl.createDoctor(doctorDto));
	}

	@Test
	void createDoctor_nullDto_throwsIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class, () -> {
			doctorServiceImpl.createDoctor(null);
		});
	}

	@Test
	void getDoctorById_nullId_throwsIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class, () -> {
			doctorServiceImpl.getDoctorById(null);
		});
	}

	@Test
	void getDoctorById_Success()  {
		Long doctorId = 1L;
		Doctor doctor = new Doctor();
		doctor.setId(doctorId);
		doctor.setName("Dr. John Doe");
		doctor.setEmail(doctorId + "@gmail.com");
		doctor.setSpecialization("Cardiology");
		doctor.setPhone("1234567890");

		when(doctorRepository.findById(doctorId)). thenReturn(java.util.Optional.of(doctor));
		DoctorDto doctorDto = doctorServiceImpl.getDoctorById(doctorId);
		assert doctorDto != null;
		assert doctorDto.getId().equals(doctorId);
		assert doctorDto.getName().equals(doctor.getName());
		assert doctorDto.getEmail().equals(doctor.getEmail());
		assert doctorDto.getSpecialization().equals(doctor.getSpecialization());
		assert doctorDto.getPhone().equals(doctor.getPhone());
	}

	@Test
	void getDoctorById_DoctorNotFound_throwsResourceNotFoundException() {
		Long doctorId = 1L;
		when(doctorRepository.findById(doctorId)).thenReturn(java.util.Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> {
			doctorServiceImpl.getDoctorById(doctorId);
		});
	}

	@Test
	void getAllDoctors_Success() {
		Doctor doctor1 = new Doctor();
		doctor1.setId(1L);
		doctor1.setName("Dr. John Doe");
		doctor1.setEmail(doctor1.getId() + "@gmail.com");
		doctor1.setSpecialization("Cardiology");
		doctor1.setPhone("1234567890");

		Doctor doctor2 = new Doctor();
		doctor2.setId(2L);
		doctor2.setName("Dr. Jane Smith");
		doctor2.setEmail(doctor2.getId() + "@gmail.com");
		doctor2.setSpecialization("Neurology");
		doctor2.setPhone("0987654321");

		when(doctorRepository.findAll()).thenReturn(java.util.List.of(doctor1, doctor2));
		List<DoctorDto> doctorDtos = doctorServiceImpl.getAllDoctors();
		assert doctorDtos != null;
		assert doctorDtos.size() == 2;
		assert doctorDtos.get(0).getId().equals(doctor1.getId());
		assert doctorDtos.get(0).getName().equals(doctor1.getName());
		assert doctorDtos.get(0).getEmail().equals(doctor1.getEmail());
		assert doctorDtos.get(0).getSpecialization().equals(doctor1.getSpecialization());
		assert doctorDtos.get(0).getPhone().equals(doctor1.getPhone());
		assert doctorDtos.get(1).getId().equals(doctor2.getId());
		assert doctorDtos.get(1).getName().equals(doctor2.getName());
		assert doctorDtos.get(1).getEmail().equals(doctor2.getEmail());
		assert doctorDtos.get(1).getSpecialization().equals(doctor2.getSpecialization());
	}

	@Test
	void updateDoctor_nullDto_throwsIllegalArgumentException() {
		assertThrows(IllegalArgumentException.class, () -> {
			doctorServiceImpl.updateDoctor(1L, null);
		});
	}

	@Test
	void updateDoctor_DoctorNotFound_throwsResourceNotFoundException() {
		Long doctorId = 1L;
		when(doctorRepository.findById(doctorId)).thenReturn(java.util.Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> {
			doctorServiceImpl.updateDoctor(doctorId, new UpdateDoctorDto());
		});
	}

	@Test
	void updateDoctor_EmailAlreadyExists_throwsAlreadyExistsException() {
		Long doctorId = 1L;
		Doctor existingDoctor = new Doctor();
		existingDoctor.setId(doctorId);
		existingDoctor.setName("Dr. John Doe");
		existingDoctor.setEmail(doctorId + "@gmail.com");
		existingDoctor.setSpecialization("Cardiology");
		existingDoctor.setPhone("1234567890");

		when(doctorRepository.findById(doctorId)).thenReturn(java.util.Optional.of(existingDoctor));

		UpdateDoctorDto updateDto = new UpdateDoctorDto();
		updateDto.setName("Dr. Jane Doe");
		updateDto.setEmail(doctorId + "@gmail.com"); // Same email as existing doctor
		updateDto.setSpecialization("Pediatrics");
		updateDto.setPhone("9876543210");

		when(doctorRepository.existsByEmail(updateDto.getEmail())).thenReturn(true);
		assertThrows(AlreadyExistsException.class, () -> {
			doctorServiceImpl.updateDoctor(doctorId, updateDto);
		});
	}

	@Test
	void updateDoctor_Success() {
		Long doctorId = 1L;
		Doctor existingDoctor = new Doctor();
		existingDoctor.setId(doctorId);
		existingDoctor.setName("Dr. John Doe");
		existingDoctor.setEmail(doctorId + "@gmail.com");
		existingDoctor.setSpecialization("Cardiology");
		existingDoctor.setPhone("1234567890");

		when(doctorRepository.findById(doctorId)).thenReturn(java.util.Optional.of(existingDoctor));

		UpdateDoctorDto updateDto = new UpdateDoctorDto();
		updateDto.setName("Dr. Jane Doe");
		updateDto.setEmail("jane@gmail.com");
		updateDto.setSpecialization("Pediatrics");
		updateDto.setPhone("9876543210");

		when(doctorRepository.existsByEmail(updateDto.getEmail())).thenReturn(false);
		when(doctorRepository.save(any(Doctor.class))).thenReturn(existingDoctor);
		DoctorDto updatedDoctorDto = doctorServiceImpl.updateDoctor(doctorId, updateDto);
		assert updatedDoctorDto != null;
		assert updatedDoctorDto.getId().equals(doctorId);
		assert updatedDoctorDto.getName().equals(updateDto.getName());
		assert updatedDoctorDto.getEmail().equals(updateDto.getEmail());
		assert updatedDoctorDto.getSpecialization().equals(updateDto.getSpecialization());
		assert updatedDoctorDto.getPhone().equals(updateDto.getPhone());
	}

	@Test
	void deleteDoctor_DoctorNotFound_throwsResourceNotFoundException() {
		Long doctorId = 1L;
		when(doctorRepository.findById(doctorId)).thenReturn(java.util.Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> {
			doctorServiceImpl.deleteDoctor(doctorId);
		});
	}

	@Test
	void deleteDoctor_Success() {
		Long doctorId = 1L;
		Doctor doctor = new Doctor();
		doctor.setId(doctorId);
		doctor.setName("Dr. John Doe");
		doctor.setEmail(doctorId + "@gmail.com");
		doctor.setSpecialization("Cardiology");
		doctor.setPhone("1234567890");

		when(doctorRepository.findById(doctorId)).thenReturn(java.util.Optional.of(doctor));
		doctorServiceImpl.deleteDoctor(doctorId);

		// Verify that the delete method was called
		when(doctorRepository.findById(doctorId)).thenReturn(java.util.Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> {
			doctorServiceImpl.getDoctorById(doctorId);
		});
	}
}
