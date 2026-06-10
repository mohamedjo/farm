package com.shabic.farm.infrastructure.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.shabic.farm.config.correlation.CorrelationId;
import com.shabic.farm.config.correlation.CorrelationIdContext;
import com.shabic.farm.domain.events.FarmCreated;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class KafkaFarmEventPublisherTest {

	@Mock private KafkaTemplate<String, String> kafkaTemplate;

	@Captor private ArgumentCaptor<ProducerRecord<String, String>> producerRecordCaptor;

	private KafkaFarmEventPublisher publisher;

	@BeforeEach
	void setUp() {
		ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
		publisher = new KafkaFarmEventPublisher(kafkaTemplate, mapper);
		ReflectionTestUtils.setField(publisher, "farmCreatedTopic", "farm.farm.created");
		ReflectionTestUtils.setField(publisher, "farmDeletedTopic", "farm.farm.deleted");
	}

	@AfterEach
	void tearDown() {
		CorrelationIdContext.clear();
	}

	@Test
	void publishFarmCreated_forwardsCorrelationIdInKafkaHeader() {
		UUID farmId = UUID.randomUUID();
		Instant timestamp = Instant.parse("2025-01-15T10:00:00Z");
		CorrelationIdContext.set("corr-farm-1");

		publisher.publishFarmCreated(new FarmCreated(farmId, "Farm A", timestamp));

		verify(kafkaTemplate).send(producerRecordCaptor.capture());
		ProducerRecord<String, String> record = producerRecordCaptor.getValue();
		assertThat(record.topic()).isEqualTo("farm.farm.created");
		assertThat(record.key()).isEqualTo(farmId.toString());
		assertThat(record.headers().lastHeader(CorrelationId.HEADER_NAME).value())
				.isEqualTo("corr-farm-1".getBytes());
	}
}
