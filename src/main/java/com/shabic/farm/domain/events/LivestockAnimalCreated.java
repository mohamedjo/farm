package com.shabic.farm.domain.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import java.time.Instant;
import java.util.UUID;

public record LivestockAnimalCreated(
		UUID eventId,
		UUID animalId,
		UUID farmId,
		String type,
		Instant timestamp
) {
	@JsonProperty(value = "eventType", access = Access.READ_ONLY)
	public String eventType() {
		return "AnimalCreated";
	}
}
