package com.shabic.farm.infrastructure.persistence.adapters;

import com.shabic.farm.domain.model.aggregate.Farm;
import com.shabic.farm.domain.repository.FarmRepository;
import com.shabic.farm.infrastructure.persistence.FarmPersistenceMapper;
import com.shabic.farm.infrastructure.persistence.repositories.FarmJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaFarmRepository implements FarmRepository {
	private final FarmJpaRepository jpa;
	private final FarmPersistenceMapper mapper;

	@Override
	public Optional<Farm> findById(UUID id) {
		return jpa.findById(id).map(mapper::toDomain);
	}

	@Override
	public Optional<Farm> findByRegisterId(String registerId) {
		if (registerId == null || registerId.isBlank()) {
			return Optional.empty();
		}
		return jpa.findByRegisterId(registerId.trim()).map(mapper::toDomain);
	}

	@Override
	public List<Farm> findAll() {
		return jpa.findAll().stream().map(mapper::toDomain).toList();
	}

	@Override
	public void save(Farm farm) {
		jpa.save(mapper.toEntity(farm));
	}

	@Override
	public void deleteById(UUID id) {
		jpa.deleteById(id);
	}

	@Override
	public boolean incrementAnimalCount(UUID farmId) {
		return jpa.incrementAnimalCount(farmId) > 0;
	}
}
