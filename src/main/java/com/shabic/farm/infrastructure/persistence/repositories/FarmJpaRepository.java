package com.shabic.farm.infrastructure.persistence.repositories;

import com.shabic.farm.infrastructure.persistence.entities.FarmEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface FarmJpaRepository extends JpaRepository<FarmEntity, UUID> {
	Optional<FarmEntity> findByRegisterId(String registerId);

	@Modifying
	@Query("UPDATE FarmEntity f SET f.numberOfAnimals = f.numberOfAnimals + 1 WHERE f.id = :farmId")
	int incrementAnimalCount(@Param("farmId") UUID farmId);
}
