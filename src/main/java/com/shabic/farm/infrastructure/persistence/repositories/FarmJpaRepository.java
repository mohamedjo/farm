package com.shabic.farm.infrastructure.persistence.repositories;

import com.shabic.farm.infrastructure.persistence.entities.FarmEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FarmJpaRepository extends JpaRepository<FarmEntity, UUID> {
	Optional<FarmEntity> findByRegisterId(String registerId);
}
