package com.suraj.healthcare.api_gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class UsersLoggingFilter extends AbstractGatewayFilterFactory<UsersLoggingFilter.Config> {

	public UsersLoggingFilter() {
		super(Config.class);
	}

	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {
			log.info("UsersLoggingFilter Pre: Request received at {}", exchange.getRequest().getURI());
			return chain.filter(exchange)
					.doOnError(error -> log.error("UsersLoggingFilter: Error processing request to {}: {}", exchange.getRequest().getURI(), error.getMessage()))
					.then(Mono.fromRunnable(() -> {
						log.info("UsersLoggingFilter Post: Response sent for request to {}", exchange.getRequest().getURI());
					}));
		};
	}

	public static class Config {
	}
}
