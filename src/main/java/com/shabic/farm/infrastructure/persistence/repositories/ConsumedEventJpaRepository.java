package com.shabic.farm.infrastructure.persistence.repositories;

import com.shabic.farm.infrastructure.persistence.entities.ConsumedEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ConsumedEventJpaRepository extends JpaRepository<ConsumedEventEntity, UUID> {}
