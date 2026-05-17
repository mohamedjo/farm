package com.shabic.farm.infrastructure.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shabic.farm.application.messaging.FarmEventPublisher;
import com.shabic.farm.domain.events.FarmCreated;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaFarmEventPublisher implements FarmEventPublisher {
	private final KafkaTemplate<String, String> kafkaTemplate;
	private final ObjectMapper objectMapper;

	@Value("${farm.kafka.topics.farm-created}")
	private String farmCreatedTopic;

	@Override
	public void publishFarmCreated(FarmCreated event) {
		try {
			String payload = objectMapper.writeValueAsString(event);
			kafkaTemplate.send(farmCreatedTopic, event.farmId().toString(), payload);
		} catch (JsonProcessingException e) {
			throw new IllegalStateException("Failed to serialize FarmCreated event", e);
		}
	}
}
