package com.suraj.healthcare.api_gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "auth")
@Data
public class RoleAccessProperties {
	private Map<String, List<String>> roleAccess;
}
