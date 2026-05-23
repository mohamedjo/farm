package com.shabic.farm.infrastructure.persistence.adapters;

import com.shabic.farm.domain.repository.ConsumedEventRepository;
import com.shabic.farm.infrastructure.persistence.entities.ConsumedEventEntity;
import com.shabic.farm.infrastructure.persistence.repositories.ConsumedEventJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaConsumedEventRepository implements ConsumedEventRepository {
	private final ConsumedEventJpaRepository jpa;

	@Override
	public boolean tryMarkConsumed(UUID eventId, Instant consumedAt) {
		if (jpa.existsById(eventId)) {
			return false;
		}
		try {
			jpa.save(ConsumedEventEntity.builder()
					.eventId(eventId)
					.consumedAt(consumedAt)
					.build());
			return true;
		} catch (DataIntegrityViolationException e) {
			return false;
		}
	}
}
