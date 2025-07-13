package com.suraj.healthcare.user_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.suraj.healthcare.user_service.dto.LoginRequestDto;
import com.suraj.healthcare.user_service.dto.SignUpRequestDto;
import com.suraj.healthcare.user_service.dto.UserDto;
import com.suraj.healthcare.user_service.enums.Role;
import com.suraj.healthcare.user_service.response.LoginResponse;
import com.suraj.healthcare.user_service.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private UserService userService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void testRegisterUser_Success() throws Exception {
		SignUpRequestDto dto = new SignUpRequestDto("John", "john@example.com", "pass123", Role.PATIENT);
		UserDto responseDto = new UserDto(1L, "John", "john@example.com", Role.PATIENT);

		when(userService.registerUser(any())).thenReturn(responseDto);

		mockMvc.perform(post("/auth/register")
						.contentType(String.valueOf(MediaType.APPLICATION_JSON))
						.content(objectMapper.writeValueAsString(dto)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.email").value("john@example.com"))
				.andExpect(jsonPath("$.role").value("PATIENT"));
	}


	@Test
	void testLoginUser_Success() throws Exception {
		LoginRequestDto dto = new LoginRequestDto("john@example.com", "pass123");
		LoginResponse response = new LoginResponse("fake-jwt");

		when(userService.loginUser(any())).thenReturn(response);

		mockMvc.perform(post("/auth/login")
						.contentType(String.valueOf(MediaType.APPLICATION_JSON))
						.content(objectMapper.writeValueAsString(dto)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.token").value("fake-jwt"));
	}

	@Test
	void testRegisterUser_InvalidEmail_BadRequest() throws Exception {
		SignUpRequestDto dto = new SignUpRequestDto("John", "invalid-email", "pass123", Role.PATIENT);

		mockMvc.perform(post("/auth/register")
						.contentType(String.valueOf(MediaType.APPLICATION_JSON))
						.content(objectMapper.writeValueAsString(dto)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.email").value("Email must be valid"));
	}
}
