package com.shabic.farm.domain.events;

import java.time.Instant;

public interface DomainEvent {
	String eventType();

	Instant timestamp();
}
