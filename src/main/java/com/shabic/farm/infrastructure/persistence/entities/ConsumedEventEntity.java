package com.shabic.farm.infrastructure.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "consumed_event")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsumedEventEntity {
	@Id
	@Column(name = "event_id", nullable = false)
	private UUID eventId;

	@Column(name = "consumed_at", nullable = false)
	private Instant consumedAt;
}
