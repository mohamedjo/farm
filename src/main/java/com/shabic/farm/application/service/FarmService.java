package com.shabic.farm.application.service;

import com.shabic.farm.application.command.RegisterFarmCommand;
import com.shabic.farm.application.command.UpdateFarmCommand;
import com.shabic.farm.application.messaging.FarmEventPublisher;
import com.shabic.farm.domain.events.FarmCreated;
import com.shabic.farm.domain.events.FarmDeleted;
import com.shabic.farm.domain.model.aggregate.Farm;
import com.shabic.farm.domain.model.valueobject.GeoLocation;
import com.shabic.farm.domain.repository.FarmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FarmService {
	private final FarmRepository farmRepo;
	private final FarmEventPublisher farmEventPublisher;

	@Transactional
	public UUID register(RegisterFarmCommand command) {
		Instant registeredAt = Instant.now();
		UUID newFarmId = UUID.randomUUID();
		String normalizedRegisterId = normalizeOptionalString(command.registerId());
		if (normalizedRegisterId != null && farmRepo.findByRegisterId(normalizedRegisterId).isPresent()) {
			throw new IllegalArgumentException("registerId already exists");
		}

		Farm registeredFarm = Farm.register(
				newFarmId,
				command.name(),
				command.region(),
				command.address(),
				toGeoLocation(command.locationLatitude(), command.locationLongitude()),
				command.email(),
				normalizedRegisterId,
				command.phoneNumber(),
				registeredAt
		);

		farmRepo.save(registeredFarm);

		farmEventPublisher.publishFarmCreated(new FarmCreated(
				registeredFarm.getId(),
				registeredFarm.getName(),
				registeredAt
		));

		return registeredFarm.getId();
	}

	@Transactional
	public Farm update(UpdateFarmCommand command) {
		Farm existingFarm = farmRepo.findById(command.farmId())
				.orElseThrow(() -> new IllegalArgumentException("farm not found"));
		RegisterFarmCommand details = command.details();

		String normalizedRegisterId = normalizeOptionalString(details.registerId());
		if (normalizedRegisterId != null) {
			farmRepo.findByRegisterId(normalizedRegisterId)
					.filter(candidate -> !candidate.getId().equals(command.farmId()))
					.ifPresent(candidate -> {
						throw new IllegalArgumentException("registerId already exists");
					});
		}

		Farm updatedFarm = Farm.rehydrate(
				existingFarm.getId(),
				details.name(),
				details.region(),
				details.address(),
				toGeoLocation(details.locationLatitude(), details.locationLongitude()),
				details.email(),
				normalizedRegisterId,
				details.phoneNumber(),
				existingFarm.getCreatedAt()
		);

		farmRepo.save(updatedFarm);
		return updatedFarm;
	}

	@Transactional
	public void delete(UUID farmId) {
		if (farmRepo.findById(farmId).isEmpty()) {
			throw new IllegalArgumentException("farm not found");
		}
		Instant deletedAt = Instant.now();
		farmEventPublisher.publishFarmDeleted(new FarmDeleted(farmId, deletedAt));
		farmRepo.deleteById(farmId);
	}

	@Transactional(readOnly = true)
	public List<Farm> getAll() {
		return farmRepo.findAll();
	}

	@Transactional(readOnly = true)
	public Farm getDetails(UUID farmId) {
		return farmRepo.findById(farmId)
				.orElseThrow(() -> new IllegalArgumentException("farm not found"));
	}

	private static GeoLocation toGeoLocation(Double latitude, Double longitude) {
		return GeoLocation.ofNullable(latitude, longitude);
	}

	private static String normalizeOptionalString(String raw) {
		if (raw == null || raw.isBlank()) {
			return null;
		}
		return raw.trim();
	}
}
