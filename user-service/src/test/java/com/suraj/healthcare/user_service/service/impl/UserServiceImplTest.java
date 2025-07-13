package com.suraj.healthcare.user_service.service.impl;

import com.suraj.healthcare.user_service.dto.LoginRequestDto;
import com.suraj.healthcare.user_service.dto.SignUpRequestDto;
import com.suraj.healthcare.user_service.dto.UserDto;
import com.suraj.healthcare.user_service.entity.User;
import com.suraj.healthcare.user_service.enums.Role;
import com.suraj.healthcare.user_service.exception.AlreadyExistsException;
import com.suraj.healthcare.user_service.exception.UserNotExistsException;
import com.suraj.healthcare.user_service.repository.UserRepository;
import com.suraj.healthcare.user_service.response.LoginResponse;
import com.suraj.healthcare.user_service.util.JwtUtil;
import com.suraj.healthcare.user_service.util.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserServiceImplTest {

	private UserRepository userRepository;
	private ModelMapper modelMapper;
	private JwtUtil jwtUtil;
	private UserServiceImpl userServiceImpl;

	@BeforeEach
	void setUp() {
		userRepository = mock(UserRepository.class);
		modelMapper = new ModelMapper();
		jwtUtil = mock(JwtUtil.class);
		userServiceImpl = new UserServiceImpl(userRepository, modelMapper, jwtUtil);
	}

	// Add test methods here to test the UserServiceImpl methods
	@Test
	void registerUser_Success() {
		SignUpRequestDto dto = new SignUpRequestDto();
		dto.setEmail("test@example.com");
		dto.setPassword("pass123");
		dto.setName("Test User");
		dto.setRole(Role.PATIENT);

		User user = modelMapper.map(dto, User.class);
		user.setId(1L);

		when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
		when(userRepository.save(any(User.class))).thenReturn(user);

		UserDto result = userServiceImpl.registerUser(dto);

		assertEquals("test@example.com", result.getEmail());
		assertEquals("Test User", result.getName());
		assertEquals(Role.PATIENT, result.getRole());
	}

	@Test
	void registerUser_AlreadyExists_ThrowsException() {
		SignUpRequestDto dto = new SignUpRequestDto();
		dto.setEmail("existing@example.com");
		dto.setPassword("password");
		dto.setName("Existing");
		dto.setRole(Role.DOCTOR);

		when(userRepository.existsByEmail(dto.getEmail())).thenReturn(true);
		assertThrows(AlreadyExistsException.class, () -> userServiceImpl.registerUser(dto));
	}

	@Test
	void registerUser_ThrowsException() {
		SignUpRequestDto dto = new SignUpRequestDto();
		dto.setEmail("existing@example.com");
		dto.setPassword("password");
		dto.setName("Existing");
		dto.setRole(Role.DOCTOR);

		when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
		when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Database error"));
		assertThrows(RuntimeException.class, () -> userServiceImpl.registerUser(dto));
	}

	@Test
	void loginUser_Success() {
		LoginRequestDto dto = new LoginRequestDto();
		dto.setEmail("test@example.com");
		dto.setPassword("pass123");

		User user = new User();
		user.setEmail(dto.getEmail());
		user.setPassword(PasswordUtil.hashPassword(dto.getPassword()));
		user.setRole(Role.ADMIN);

		when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(user));
		when(jwtUtil.generateAccessToken(user.getEmail(), user.getRole().name()))
				.thenReturn("fake-jwt-token");

		LoginResponse response = userServiceImpl.loginUser(dto);

		assertEquals("fake-jwt-token", response.getToken());
	}

	@Test
	void loginUser_UserNotFound_ThrowsException() {
		LoginRequestDto dto = new LoginRequestDto();
		dto.setEmail("unknown@example.com");
		dto.setPassword("whatever");

		when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());

		assertThrows(UserNotExistsException.class, () -> userServiceImpl.loginUser(dto));
	}


	@Test
	void loginUser_InvalidPassword_ThrowsException() {
		LoginRequestDto dto = new LoginRequestDto();
		dto.setEmail("test@example.com");
		dto.setPassword("wrong");

		User user = new User();
		user.setEmail(dto.getEmail());
		user.setPassword(PasswordUtil.hashPassword("correct"));
		user.setRole(Role.ADMIN);

		when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(user));

		assertThrows(IllegalArgumentException.class, () -> userServiceImpl.loginUser(dto));
	}
}
