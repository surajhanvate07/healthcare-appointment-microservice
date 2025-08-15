package com.suraj.healthcare.appointment_service.interceptor;

import com.suraj.healthcare.appointment_service.context.UserContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class UserInterceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String email = request.getHeader("X-User-Email");
		String role = request.getHeader("X-User-Role");
		Long userId = null;
		try {
			userId = Long.parseLong(request.getHeader("X-User-Id"));
		} catch (NumberFormatException e) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID format");
			return false;
		}

		if (email != null && role != null) {
			UserContextHolder.setUser(new UserContextHolder.UserInfo(email, role, userId));
		}
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		UserContextHolder.clear();
	}
}
