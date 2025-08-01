package com.suraj.healthcare.patient_service.context;

import org.springframework.stereotype.Component;

@Component
public class UserContextHolder {

//	Using spring's RequestContextHolder to manage user context in a thread-safe manner'
//	private static final String USER_EMAIL_HEADER = "X-User-Email";
//	private static final String USER_ROLE_HEADER = "X-User-Role";
//
//	public static String getUserEmail() {
//		return getHeader(USER_EMAIL_HEADER);
//	}
//
//	public static String getUserRole() {
//		return getHeader(USER_ROLE_HEADER);
//	}
//
//	private static String getHeader(String headerName) {
//		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
//		if (requestAttributes == null) return null;
//
//		return ((ServletRequestAttributes) requestAttributes)
//				.getRequest()
//				.getHeader(headerName);
//	}

	private static final ThreadLocal<UserInfo> userThreadLocal = new ThreadLocal<>();

	public static void setUser(UserInfo user) {
		userThreadLocal.set(user);
	}

	public static UserInfo getCurrentUser() {
		return userThreadLocal.get();
	}

	public static void clear() {
		userThreadLocal.remove();
	}

	public record UserInfo(String email, String role) {
	}

}
