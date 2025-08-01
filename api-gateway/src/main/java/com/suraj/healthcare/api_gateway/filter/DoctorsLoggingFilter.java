package com.suraj.healthcare.api_gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class DoctorsLoggingFilter extends AbstractGatewayFilterFactory<DoctorsLoggingFilter.Config> {
	public DoctorsLoggingFilter() {
		super(Config.class);
	}

	@Override
	public GatewayFilter apply(Config config) {
		return ((exchange, chain) -> {
			log.info("DoctorsLoggingFilter Pre: Request received at {}", exchange.getRequest().getURI());
			return chain.filter(exchange)
					.doOnError(error -> log.error("DoctorsLoggingFilter: Error processing request to {}: {}", exchange.getRequest().getURI(), error.getMessage()))
					.then(Mono.fromRunnable(() -> {
						log.info("DoctorsLoggingFilter Post: Response sent for request to {}", exchange.getRequest().getURI());
					}));
		});
	}

	public static class Config {

	}
}
