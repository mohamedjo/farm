package com.shabic.farm.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "farm.openapi")
public record OpenApiDocProperties(
		String title,
		String version,
		String description,
		String contactName,
		String serverUrl,
		String serverDescription,
		String tagFarmsName,
		String tagFarmsDescription,
		String exampleRegisterFullName,
		String exampleRegisterMinimalName,
		String exampleUpdateName,
		String responseFarmIdExample
) {
}
