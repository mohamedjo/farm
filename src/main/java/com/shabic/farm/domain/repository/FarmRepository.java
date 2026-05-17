package com.shabic.farm.domain.repository;

import com.shabic.farm.domain.model.aggregate.Farm;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FarmRepository {
	Optional<Farm> findById(UUID id);

	Optional<Farm> findByRegisterId(String registerId);

	List<Farm> findAll();

	void save(Farm farm);

	void deleteById(UUID id);
}
