package com.shabic.farm.application.service;

import com.shabic.farm.domain.events.LivestockAnimalCreated;
import com.shabic.farm.domain.repository.ConsumedEventRepository;
import com.shabic.farm.domain.repository.FarmRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FarmAnimalCountServiceTest {

	@Mock private ConsumedEventRepository consumedEventRepo;
	@Mock private FarmRepository farmRepo;

	private FarmAnimalCountService service;

	@BeforeEach
	void setUp() {
		service = new FarmAnimalCountService(consumedEventRepo, farmRepo);
	}

	@Test
	void onAnimalCreated_incrementsCount_whenEventIsNew() {
		UUID eventId = UUID.randomUUID();
		UUID farmId = UUID.randomUUID();
		LivestockAnimalCreated event = new LivestockAnimalCreated(
				eventId,
				UUID.randomUUID(),
				farmId,
				"Cow",
				Instant.parse("2025-01-15T10:00:00Z")
		);
		when(consumedEventRepo.tryMarkConsumed(eq(eventId), any(Instant.class))).thenReturn(true);
		when(farmRepo.incrementAnimalCount(farmId)).thenReturn(true);

		service.onAnimalCreated(event);

		verify(farmRepo).incrementAnimalCount(farmId);
	}

	@Test
	void onAnimalCreated_isIdempotent_whenEventAlreadyConsumed() {
		UUID eventId = UUID.randomUUID();
		UUID farmId = UUID.randomUUID();
		LivestockAnimalCreated event = new LivestockAnimalCreated(
				eventId,
				UUID.randomUUID(),
				farmId,
				"Cow",
				Instant.now()
		);
		when(consumedEventRepo.tryMarkConsumed(eq(eventId), any(Instant.class))).thenReturn(false);

		service.onAnimalCreated(event);

		verify(farmRepo, never()).incrementAnimalCount(any());
	}

	@Test
	void onAnimalCreated_throwsWhenFarmNotFound_afterMarkingConsumed() {
		UUID eventId = UUID.randomUUID();
		UUID farmId = UUID.randomUUID();
		LivestockAnimalCreated event = new LivestockAnimalCreated(
				eventId,
				UUID.randomUUID(),
				farmId,
				"Cow",
				Instant.now()
		);
		when(consumedEventRepo.tryMarkConsumed(eq(eventId), any(Instant.class))).thenReturn(true);
		when(farmRepo.incrementAnimalCount(farmId)).thenReturn(false);

		assertThatThrownBy(() -> service.onAnimalCreated(event))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("farm not found");
	}
}
