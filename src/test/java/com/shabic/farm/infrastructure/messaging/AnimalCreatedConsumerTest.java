package com.shabic.farm.infrastructure.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.shabic.farm.application.service.FarmAnimalCountService;
import com.shabic.farm.domain.events.LivestockAnimalCreated;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class AnimalCreatedConsumerTest {

	@Mock private FarmAnimalCountService farmAnimalCountService;

	@Captor private ArgumentCaptor<LivestockAnimalCreated> eventCaptor;

	private AnimalCreatedConsumer consumer;

	@BeforeEach
	void setUp() {
		ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
		consumer = new AnimalCreatedConsumer(mapper, farmAnimalCountService);
	}

	@Test
	void onAnimalCreated_delegatesToService() {
		UUID eventId = UUID.randomUUID();
		UUID animalId = UUID.randomUUID();
		UUID farmId = UUID.randomUUID();
		Instant timestamp = Instant.parse("2025-01-15T10:00:00Z");
		String payload = """
				{
				  "eventId": "%s",
				  "animalId": "%s",
				  "farmId": "%s",
				  "type": "Cow",
				  "timestamp": "%s",
				  "eventType": "AnimalCreated"
				}
				""".formatted(eventId, animalId, farmId, timestamp);

		consumer.onAnimalCreated(payload);

		verify(farmAnimalCountService).onAnimalCreated(eventCaptor.capture());
		LivestockAnimalCreated event = eventCaptor.getValue();
		assertThat(event.eventId()).isEqualTo(eventId);
		assertThat(event.farmId()).isEqualTo(farmId);
		assertThat(event.animalId()).isEqualTo(animalId);
	}

	@Test
	void onAnimalCreated_skipsWhenRequiredFieldsMissing() {
		consumer.onAnimalCreated("{\"animalId\": \"%s\"}".formatted(UUID.randomUUID()));

		verifyNoInteractions(farmAnimalCountService);
	}

	@Test
	void onAnimalCreated_wrapsDeserializationErrors() {
		assertThatThrownBy(() -> consumer.onAnimalCreated("not-json"))
				.isInstanceOf(RuntimeException.class)
				.hasMessageContaining("Failed to consume AnimalCreated event");
	}
}
