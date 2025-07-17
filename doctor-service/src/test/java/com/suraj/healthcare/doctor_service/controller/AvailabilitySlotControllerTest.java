package com.suraj.healthcare.doctor_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.suraj.healthcare.doctor_service.dto.AvailabilitySlotDto;
import com.suraj.healthcare.doctor_service.service.AvailabilitySlotService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AvailabilitySlotController.class)
public class AvailabilitySlotControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private AvailabilitySlotService availabilitySlotService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void test_createSlot_Success() throws Exception {
		AvailabilitySlotDto slotDto = AvailabilitySlotDto.builder()
				.date(LocalDate.now())
				.startTime(LocalTime.parse("09:00"))
				.endTime(LocalTime.parse("10:00"))
				.doctorId(1L)
				.build();

		when(availabilitySlotService.createSlot(any(AvailabilitySlotDto.class))).thenReturn(slotDto);

		mockMvc.perform(post("/availability-slots/create")
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(slotDto)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.date").value(slotDto.getDate().toString()))
				.andExpect(jsonPath("$.startTime").value(slotDto.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm:ss"))))
				.andExpect(jsonPath("$.endTime").value(slotDto.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm:ss"))))
				.andExpect(jsonPath("$.doctorId").value(slotDto.getDoctorId()));

	}

	@Test
	void test_createSlot_InvalidDate() throws Exception {
		AvailabilitySlotDto slotDto = AvailabilitySlotDto.builder()
				.date(null) // Invalid date
				.startTime(LocalTime.parse("09:00"))
				.endTime(LocalTime.parse("10:00"))
				.doctorId(1L)
				.build();

		mockMvc.perform(post("/availability-slots/create")
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(slotDto)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.date").value("Date is required"));
	}

	@Test
	void test_getSlotsByDoctor_Success() throws Exception {
		AvailabilitySlotDto slotDto = AvailabilitySlotDto.builder()
				.date(LocalDate.now())
				.startTime(LocalTime.parse("09:00"))
				.endTime(LocalTime.parse("10:00"))
				.doctorId(1L)
				.build();

		when(availabilitySlotService.getSlotsByDoctorId(1L)).thenReturn(List.of(slotDto));

		mockMvc.perform(get("/availability-slots/doctor/by-doctorId/{doctorId}", 1L)
						.contentType("application/json"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].date").value(slotDto.getDate().toString()))
				.andExpect(jsonPath("$[0].startTime").value(slotDto.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm:ss"))))
				.andExpect(jsonPath("$[0].endTime").value(slotDto.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm:ss"))))
				.andExpect(jsonPath("$[0].doctorId").value(slotDto.getDoctorId()));
	}

	@Test
	void test_getSlotsByDoctorAndDate_Success() throws Exception {
		AvailabilitySlotDto slotDto = AvailabilitySlotDto.builder()
				.date(LocalDate.now())
				.startTime(LocalTime.parse("09:00"))
				.endTime(LocalTime.parse("10:00"))
				.doctorId(1L)
				.build();

		when(availabilitySlotService.getSlotsByDoctorIdAndDate(1L, LocalDate.now())).thenReturn(List.of(slotDto));

		mockMvc.perform(get("/availability-slots/doctor/by-date/{date}", LocalDate.now())
						.param("doctorId", "1")
						.contentType("application/json"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].date").value(slotDto.getDate().toString()))
				.andExpect(jsonPath("$[0].startTime").value(slotDto.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm:ss"))))
				.andExpect(jsonPath("$[0].endTime").value(slotDto.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm:ss"))))
				.andExpect(jsonPath("$[0].doctorId").value(slotDto.getDoctorId()));
	}

	@Test
	void test_deleteSlot_Success() throws Exception {
		Long slotId = 1L;

		mockMvc.perform(delete("/availability-slots/{slotId}", slotId)
						.contentType("application/json"))
				.andExpect(status().isNoContent());

	}
}
