package com.shabic.farm.config.correlation;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.apache.kafka.common.record.TimestampType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class CorrelationIdRecordInterceptorTest {

	private final CorrelationIdRecordInterceptor interceptor = new CorrelationIdRecordInterceptor();

	@AfterEach
	void tearDown() {
		CorrelationIdContext.clear();
		MDC.clear();
	}

	@Test
	void intercept_setsCorrelationIdFromKafkaHeader() {
		var headers = new RecordHeaders();
		headers.add(
				CorrelationId.HEADER_NAME,
				"corr-from-livestock".getBytes(StandardCharsets.UTF_8));
		ConsumerRecord<Object, Object> record = new ConsumerRecord<>(
				"livestock.animal.created",
				0,
				0L,
				0L,
				TimestampType.CREATE_TIME,
				-1,
				-1,
				"key",
				"payload",
				headers,
				Optional.empty());

		interceptor.intercept(record, mock());
		assertThat(CorrelationIdContext.get()).contains("corr-from-livestock");
		assertThat(MDC.get(CorrelationId.MDC_KEY)).isEqualTo("corr-from-livestock");

		interceptor.success(record, mock());
		assertThat(CorrelationIdContext.get()).isEmpty();
		assertThat(MDC.get(CorrelationId.MDC_KEY)).isNull();
	}
}
