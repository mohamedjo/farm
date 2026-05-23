package com.shabic.farm.application.service;

import com.shabic.farm.domain.events.LivestockAnimalCreated;
import com.shabic.farm.domain.repository.ConsumedEventRepository;
import com.shabic.farm.domain.repository.FarmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FarmAnimalCountService {
	private final ConsumedEventRepository consumedEventRepo;
	private final FarmRepository farmRepo;

	@Transactional
	public void onAnimalCreated(LivestockAnimalCreated event) {
		UUID eventId = Objects.requireNonNull(event.eventId(), "eventId");
		UUID farmId = Objects.requireNonNull(event.farmId(), "farmId");

		if (!consumedEventRepo.tryMarkConsumed(eventId, Instant.now())) {
			return;
		}

		if (!farmRepo.incrementAnimalCount(farmId)) {
			throw new IllegalArgumentException("farm not found: " + farmId);
		}
	}
}
