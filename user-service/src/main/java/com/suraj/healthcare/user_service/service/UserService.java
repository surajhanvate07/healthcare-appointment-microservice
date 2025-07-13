package com.suraj.healthcare.user_service.service;

import com.suraj.healthcare.user_service.dto.LoginRequestDto;
import com.suraj.healthcare.user_service.dto.SignUpRequestDto;
import com.suraj.healthcare.user_service.dto.UserDto;
import com.suraj.healthcare.user_service.response.LoginResponse;

public interface UserService {

	UserDto registerUser(SignUpRequestDto signUpRequestDto);

	LoginResponse loginUser(LoginRequestDto loginRequestDto);
}
