package com.suraj.healthcare.user_service.dto;

import com.suraj.healthcare.user_service.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
	private Long id;
	private String name;
	private String email;
	private Role role;
}
