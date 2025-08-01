package com.suraj.healthcare.user_service.service.impl;

import com.suraj.healthcare.user_service.client.PatientClient;
import com.suraj.healthcare.user_service.dto.CreatePatientRequestDto;
import com.suraj.healthcare.user_service.dto.LoginRequestDto;
import com.suraj.healthcare.user_service.dto.SignUpRequestDto;
import com.suraj.healthcare.user_service.dto.UserDto;
import com.suraj.healthcare.user_service.entity.User;
import com.suraj.healthcare.user_service.enums.Role;
import com.suraj.healthcare.user_service.exception.AlreadyExistsException;
import com.suraj.healthcare.user_service.exception.UserNotExistsException;
import com.suraj.healthcare.user_service.repository.UserRepository;
import com.suraj.healthcare.user_service.response.LoginResponse;
import com.suraj.healthcare.user_service.service.UserService;
import com.suraj.healthcare.user_service.util.JwtUtil;
import com.suraj.healthcare.user_service.util.PasswordUtil;
import io.github.resilience4j.retry.annotation.Retry;
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
	private final PatientClient patientClient;

	@Retry(name = "patientServiceRetry", fallbackMethod = "handlePatientServiceFailure")
	public void registerPatient(CreatePatientRequestDto dto) {
		patientClient.createPatient(dto);  // Feign client call
	}

	private void handlePatientServiceFailure(CreatePatientRequestDto dto, Exception ex) {
		// Log failure, persist to outbox, or raise alert
		log.error("Failed to create patient in PatientService. Will retry later. Reason: {}", ex.getMessage());
		// Optional: Save failed event to DB or queue
	}


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

			if (savedUser.getRole() == Role.PATIENT) {
				log.info("Creating patient profile for user with ID: {}", savedUser.getId());
				// Assuming a PatientClient is available to create a patient profile
				CreatePatientRequestDto createPatientRequestDto = CreatePatientRequestDto.builder()
						.name(savedUser.getName())
						.email(savedUser.getEmail())
						.phone(savedUser.getPhone())
						.dob(savedUser.getDob())
						.internalRequest(true)
						.build();

				registerPatient(createPatientRequestDto);

			}
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
