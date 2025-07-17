package com.suraj.healthcare.doctor_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.suraj.healthcare.doctor_service.dto.DoctorDto;
import com.suraj.healthcare.doctor_service.dto.UpdateDoctorDto;
import com.suraj.healthcare.doctor_service.service.DoctorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DoctorController.class)
public class DoctorControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private DoctorService doctorService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void testCreateDoctor_Success() throws Exception {
		// Implement test logic for creating a doctor
		DoctorDto doctorDto = new DoctorDto();
		doctorDto.setName("Dr. Smith");
		doctorDto.setSpecialization("Cardiology");
		doctorDto.setEmail("test@gmail.com");
		doctorDto.setPhone("1234567890");

		when(doctorService.createDoctor(any(DoctorDto.class))).thenReturn(doctorDto);

		mockMvc.perform(post("/doctors/create-doctor")
						.contentType(String.valueOf(MediaType.APPLICATION_JSON))
						.content(objectMapper.writeValueAsString(doctorDto)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Dr. Smith"))
				.andExpect(jsonPath("$.specialization").value("Cardiology"))
				.andExpect(jsonPath("$.email").value("test@gmail.com"));
	}

	@Test
	void testCreateDoctor_InvalidEmail() throws Exception {
		// Implement test logic for creating a doctor with invalid email
		DoctorDto doctorDto = new DoctorDto();
		doctorDto.setName("Dr. Smith");
		doctorDto.setSpecialization("Cardiology");
		doctorDto.setEmail("invalid-email");
		doctorDto.setPhone("1234567890");

		mockMvc.perform(post("/doctors/create-doctor")
						.contentType(String.valueOf(MediaType.APPLICATION_JSON))
						.content(objectMapper.writeValueAsString(doctorDto)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void test_getAllDoctors_Success() throws Exception {
		DoctorDto doctorDto = new DoctorDto();
		doctorDto.setName("Dr. Smith");
		doctorDto.setSpecialization("Cardiology");
		doctorDto.setEmail("smith@gmail.com");
		doctorDto.setPhone("1234567890");

		DoctorDto doctorDto2 = new DoctorDto();
		doctorDto2.setName("Dr. John");
		doctorDto2.setSpecialization("Neurology");
		doctorDto2.setEmail("john@gmail.com");
		doctorDto2.setPhone("0987654321");

		when(doctorService.getAllDoctors()).thenReturn(List.of(doctorDto, doctorDto2));

		mockMvc.perform(get("/doctors/findAll"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].name").value("Dr. Smith"))
				.andExpect(jsonPath("$[0].specialization").value("Cardiology"))
				.andExpect(jsonPath("$[0].email").value("smith@gmail.com"))
				.andExpect(jsonPath("$[1].name").value("Dr. John"))
				.andExpect(jsonPath("$[1].specialization").value("Neurology"));
	}

	@Test
	void test_getDoctorById_Success() throws Exception {
		Long doctorId = 1L;
		DoctorDto doctorDto = new DoctorDto();
		doctorDto.setId(doctorId);
		doctorDto.setName("Dr. Smith");
		doctorDto.setSpecialization("Cardiology");
		doctorDto.setEmail("smith@gmail.com");
		doctorDto.setPhone("1234567890");

		when(doctorService.getDoctorById(doctorId)).thenReturn(doctorDto);

		mockMvc.perform(get("/doctors/findById/{id}", doctorId))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(doctorId))
				.andExpect(jsonPath("$.name").value("Dr. Smith"))
				.andExpect(jsonPath("$.specialization").value("Cardiology"))
				.andExpect(jsonPath("$.email").value("smith@gmail.com"));
	}

	@Test
	void test_getDoctorById_NotFound() throws Exception {
		Long doctorId = 1L;

		when(doctorService.getDoctorById(doctorId)).thenReturn(null);

		mockMvc.perform(get("/doctors/findById/{id}", doctorId))
				.andExpect(status().isNotFound());
	}

	@Test
	void testDeleteDoctor_Success() throws Exception {
		Long doctorId = 1L;

		mockMvc.perform(delete("/doctors/delete-doctor/{id}", doctorId))
				.andExpect(status().isNoContent());
	}

	@Test
	void testDeleteDoctor_NotFound() throws Exception {
		Long doctorId = 1L;

		doctorService.deleteDoctor(doctorId);

		mockMvc.perform(delete("/doctors/delete-doctor/{id}", doctorId))
				.andExpect(status().isNoContent());
	}

	@Test
	void updateDoctor_ShouldReturnUpdatedDoctor_WhenDoctorExists() throws Exception {
		Long doctorId = 1L;
		UpdateDoctorDto updateDoctorDto = new UpdateDoctorDto();
		updateDoctorDto.setName("Updated Name");
		updateDoctorDto.setSpecialization("Updated Specialization");
		updateDoctorDto.setEmail("updated@gmail.com");

		DoctorDto updatedDoctorDto = new DoctorDto();
		updatedDoctorDto.setId(doctorId);
		updatedDoctorDto.setName("Updated Name");
		updatedDoctorDto.setSpecialization("Updated Specialization");
		updatedDoctorDto.setEmail("updated@gmail.com");
		updatedDoctorDto.setPhone("9876543210");

		when(doctorService.updateDoctor(doctorId, updateDoctorDto)).thenReturn(updatedDoctorDto);

		mockMvc.perform(put("/doctors/update-doctor/{id}", doctorId)
						.contentType(String.valueOf(MediaType.APPLICATION_JSON))
						.content(objectMapper.writeValueAsString(updateDoctorDto)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(doctorId))
				.andExpect(jsonPath("$.name").value("Updated Name"))
				.andExpect(jsonPath("$.specialization").value("Updated Specialization"))
				.andExpect(jsonPath("$.email").value("updated@gmail.com"));
	}

	@Test
	void updateDoctor_ShouldReturnNotFound_WhenDoctorDoesNotExist() throws Exception {
		Long doctorId = 999L;
		UpdateDoctorDto updateDoctorDto = new UpdateDoctorDto();
		updateDoctorDto.setName("Nonexistent Doctor");
		updateDoctorDto.setSpecialization("Unknown");

		when(doctorService.updateDoctor(doctorId, updateDoctorDto)).thenReturn(null);

		mockMvc.perform(put("/doctors/update-doctor/{id}", doctorId)
						.contentType(String.valueOf(MediaType.APPLICATION_JSON))
						.content(objectMapper.writeValueAsString(updateDoctorDto)))
				.andExpect(status().isNotFound());
	}
}
