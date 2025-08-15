package com.suraj.healthcare.api_gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.suraj.healthcare.api_gateway.config.RoleAccessProperties;
import com.suraj.healthcare.api_gateway.response.ErrorResponse;
import com.suraj.healthcare.api_gateway.service.JwtService;
import io.jsonwebtoken.JwtException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Slf4j
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

	private final JwtService jwtService;
	private final RoleAccessProperties roleAccessProperties;

	public AuthenticationFilter(JwtService jwtService, RoleAccessProperties roleAccessProperties) {
		super(Config.class);
		this.jwtService = jwtService;
		this.roleAccessProperties = roleAccessProperties;
	}

	@Override
	public GatewayFilter apply(Config config) {
		return ((exchange, chain) -> {
			log.info("Authentication filter executed for request pre: {}", exchange.getRequest().getURI());

			String authorizationHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
			if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
				log.warn("Invalid or missing Authorization header");
				exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
				return unauthorized(exchange.getResponse(), "Missing or invalid Authorization header");
			}

			String jwtToken = authorizationHeader.substring(7);
			String userEmailId = null;
			String userRole = null;
			Long userId = null;
			try {
				userEmailId = jwtService.extractUserEmailId(jwtToken);
				userRole = jwtService.extractUserRole(jwtToken);
				userId = jwtService.extractUserId(jwtToken);
			} catch (JwtException | IllegalArgumentException e) {
				return unauthorized(exchange.getResponse(), "Invalid JWT token");
			}

			if (userEmailId == null || userRole == null) {
				return unauthorized(exchange.getResponse(), "Invalid JWT token");
			}

			log.info("Extracted user email ID: {}", userEmailId);
			log.info("Extracted user role: {}", userRole);
			log.info("Extracted user ID: {}", userId);

			String path = exchange.getRequest().getURI().getPath();

			// Role-based access control
			if (!isAccessAllowed(path, userRole)) {
				log.warn("Access denied for role: {} on path: {}", userRole, path);
				return unauthorized(exchange.getResponse(), "Access denied for role: " + userRole);
			}

			ServerWebExchange serverWebExchange = exchange.mutate()
					.request(exchange.getRequest().mutate()
							.header("X-User-Email", userEmailId)
							.header("X-User-Role", userRole)
							.header("X-User-Id", String.valueOf(userId))
							.build())
					.build();

			return chain.filter(serverWebExchange);
		});
	}

	private Mono<Void> unauthorized(ServerHttpResponse response, String message) {
		response.setStatusCode(HttpStatus.UNAUTHORIZED);
		response.getHeaders().add("Content-Type", "application/json");

		String timestamp = java.time.ZonedDateTime.now().toString();
		String path = response.getHeaders().getOrigin(); // fallback for path
		if (path == null) path = "unknown";

		ErrorResponse errorResponse = new ErrorResponse(
				timestamp,
				HttpStatus.UNAUTHORIZED.value(),
				HttpStatus.UNAUTHORIZED.getReasonPhrase(),
				message,
				path
		);

		try {
			byte[] responseBytes = new ObjectMapper().writeValueAsBytes(errorResponse);
			return response.writeWith(Mono.just(response.bufferFactory().wrap(responseBytes)));
		} catch (JsonProcessingException e) {
			byte[] fallback = ("{\"error\": \"" + message + "\"}").getBytes();
			return response.writeWith(Mono.just(response.bufferFactory().wrap(fallback)));
		}
	}

	private boolean isAccessAllowed(String path, String role) {
		List<String> allowedPaths = roleAccessProperties.getRoleAccess().get(role.toUpperCase());

		if (allowedPaths == null) {
			return false;
		}

		for (String allowedPath : allowedPaths) {
			if (path.startsWith(allowedPath)) {
				return true;
			}
		}
		return false;
	}

	@Data
	public static class Config {
	}

}
