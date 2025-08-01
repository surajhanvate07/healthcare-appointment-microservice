package com.suraj.healthcare.api_gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class PatientsLoggingFilter extends AbstractGatewayFilterFactory<PatientsLoggingFilter.Config> {

	public PatientsLoggingFilter() {
		super(Config.class);
	}

	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {
			log.info("PatientsLoggingFilter Pre: Request received at {}", exchange.getRequest().getURI());
			return chain.filter(exchange)
					.doOnError(error -> log.error("PatientsLoggingFilter: Error processing request to {}: {}", exchange.getRequest().getURI(), error.getMessage()))
					.then(Mono.fromRunnable(() -> {
						log.info("PatientsLoggingFilter Post: Response sent for request to {}", exchange.getRequest().getURI());
					}));
		};
	}

	public static class Config {
	}
}
