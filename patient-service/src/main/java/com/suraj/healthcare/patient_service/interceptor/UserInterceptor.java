package com.suraj.healthcare.patient_service.interceptor;

import com.suraj.healthcare.patient_service.context.UserContextHolder;
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

		if (email != null && role != null) {
			UserContextHolder.setUser(new UserContextHolder.UserInfo(email, role));
		}
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		UserContextHolder.clear();
	}
}
