package com.suraj.healthcare.user_service.controller;

import com.suraj.healthcare.user_service.dto.LoginRequestDto;
import com.suraj.healthcare.user_service.dto.SignUpRequestDto;
import com.suraj.healthcare.user_service.dto.UserDto;
import com.suraj.healthcare.user_service.response.LoginResponse;
import com.suraj.healthcare.user_service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

	private final UserService userService;

	@PostMapping("/register")
	public ResponseEntity<UserDto> register(@Valid @RequestBody SignUpRequestDto signUpRequestDto) {
		return ResponseEntity.ok(userService.registerUser(signUpRequestDto));
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
		return ResponseEntity.ok(userService.loginUser(loginRequestDto));
	}
}
