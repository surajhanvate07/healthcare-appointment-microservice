package com.suraj.healthcare.user_service.service.impl;

import com.suraj.healthcare.user_service.dto.LoginRequestDto;
import com.suraj.healthcare.user_service.dto.SignUpRequestDto;
import com.suraj.healthcare.user_service.dto.UserDto;
import com.suraj.healthcare.user_service.entity.User;
import com.suraj.healthcare.user_service.exception.AlreadyExistsException;
import com.suraj.healthcare.user_service.exception.UserNotExistsException;
import com.suraj.healthcare.user_service.repository.UserRepository;
import com.suraj.healthcare.user_service.response.LoginResponse;
import com.suraj.healthcare.user_service.service.UserService;
import com.suraj.healthcare.user_service.util.JwtUtil;
import com.suraj.healthcare.user_service.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final ModelMapper modelMapper;
	private final JwtUtil jwtUtil;

	@Override
	public UserDto registerUser(SignUpRequestDto signUpRequestDto) {
		log.info("Registering user with email: {}", signUpRequestDto.getEmail());

		User user = modelMapper.map(signUpRequestDto, User.class);

		if (userRepository.existsByEmail(user.getEmail())) {
			log.warn("User with email {} already exists", user.getEmail());
			throw new AlreadyExistsException("User with this email already exists");
		}

		user.setPassword(PasswordUtil.hashPassword(signUpRequestDto.getPassword()));

		try {
			User savedUser = userRepository.save(user);
			log.info("User registered successfully with ID: {}", savedUser.getId());
			return modelMapper.map(savedUser, UserDto.class);
		} catch (Exception e) {
			log.error("Error occurred while saving user: {}", e.getMessage());
			throw new RuntimeException("Failed to register user", e);
		}
	}

	@Override
	public LoginResponse loginUser(LoginRequestDto loginRequestDto) {
		log.info("Logging in user with email: {}", loginRequestDto.getEmail());

		User user = userRepository.findByEmail(loginRequestDto.getEmail())
				.orElseThrow(() -> new UserNotExistsException("User with this email: " + loginRequestDto.getEmail() + "does not exist"));

		if (!PasswordUtil.checkPassword(loginRequestDto.getPassword(), user.getPassword())) {
			log.warn("Invalid password for user with email: {}", loginRequestDto.getEmail());
			throw new IllegalArgumentException("Invalid password");
		}
		log.info("User logged in successfully with email: {}", loginRequestDto.getEmail());

		String token = jwtUtil.generateAccessToken(user.getEmail(), user.getRole().name());
		return new LoginResponse(token);
	}
}
