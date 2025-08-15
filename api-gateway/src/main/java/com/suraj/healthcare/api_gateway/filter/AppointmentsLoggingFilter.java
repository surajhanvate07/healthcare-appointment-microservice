package com.suraj.healthcare.api_gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AppointmentsLoggingFilter extends AbstractGatewayFilterFactory<AppointmentsLoggingFilter.Config> {
	public AppointmentsLoggingFilter() {
		super(Config.class);
	}

	@Override
	public GatewayFilter apply(AppointmentsLoggingFilter.Config config) {
		return ((exchange, chain) -> {
			log.info("AppointmentsLoggingFilter Pre: Request received at {}", exchange.getRequest().getURI());
			return chain.filter(exchange)
					.doOnError(error -> log.error("AppointmentsLoggingFilter: Error processing request to {}: {}", exchange.getRequest().getURI(), error.getMessage()))
					.then(Mono.fromRunnable(() -> {
						log.info("AppointmentsLoggingFilter Post: Response sent for request to {}", exchange.getRequest().getURI());
					}));
		});
	}

	public static class Config {

	}
}
