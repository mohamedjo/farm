package com.shabic.farm.domain.repository;

import java.time.Instant;
import java.util.UUID;

public interface ConsumedEventRepository {
	/**
	 * @return true if this event was recorded for the first time; false if already consumed.
	 */
	boolean tryMarkConsumed(UUID eventId, Instant consumedAt);
}
