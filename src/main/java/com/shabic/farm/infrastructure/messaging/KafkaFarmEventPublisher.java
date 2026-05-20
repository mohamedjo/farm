package com.shabic.farm.infrastructure.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shabic.farm.application.messaging.FarmEventPublisher;
import com.shabic.farm.domain.events.FarmCreated;
import com.shabic.farm.domain.events.FarmDeleted;
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

	@Value("${farm.kafka.topics.farm-deleted}")
	private String farmDeletedTopic;

	@Override
	public void publishFarmCreated(FarmCreated event) {
		publish(farmCreatedTopic, event.farmId().toString(), event);
	}

	@Override
	public void publishFarmDeleted(FarmDeleted event) {
		publish(farmDeletedTopic, event.farmId().toString(), event);
	}

	private void publish(String topic, String key, Object event) {
		try {
			String payload = objectMapper.writeValueAsString(event);
			kafkaTemplate.send(topic, key, payload);
		} catch (JsonProcessingException e) {
			throw new IllegalStateException("Failed to serialize farm event", e);
		}
	}
}
