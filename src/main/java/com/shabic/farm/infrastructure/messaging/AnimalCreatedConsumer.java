package com.shabic.farm.infrastructure.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shabic.farm.application.service.FarmAnimalCountService;
import com.shabic.farm.domain.events.LivestockAnimalCreated;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AnimalCreatedConsumer {
	private final ObjectMapper objectMapper;
	private final FarmAnimalCountService farmAnimalCountService;

	@KafkaListener(topics = "${farm.kafka.topics.animal-created}")
	public void onAnimalCreated(String payload) {
		try {
			LivestockAnimalCreated event = objectMapper.readValue(payload, LivestockAnimalCreated.class);
			if (event.eventId() == null || event.farmId() == null) {
				return;
			}
			farmAnimalCountService.onAnimalCreated(event);
		} catch (Exception e) {
			throw new RuntimeException("Failed to consume AnimalCreated event", e);
		}
	}
}
