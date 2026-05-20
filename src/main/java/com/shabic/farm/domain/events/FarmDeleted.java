package com.shabic.farm.domain.events;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.UUID;

public record FarmDeleted(
		UUID farmId,
		Instant timestamp
) implements DomainEvent {
	@JsonProperty("eventType")
	@Override
	public String eventType() {
		return "FarmDeleted";
	}
}
