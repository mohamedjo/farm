package com.shabic.farm.domain.events;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.UUID;

public record FarmCreated(
		UUID farmId,
		String name,
		Instant timestamp
) implements DomainEvent {
	@JsonProperty("eventType")
	@Override
	public String eventType() {
		return "FarmCreated";
	}
}
