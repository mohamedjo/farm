package com.shabic.farm.application.service;

import com.shabic.farm.application.command.RegisterFarmCommand;
import com.shabic.farm.application.command.UpdateFarmCommand;
import com.shabic.farm.application.messaging.FarmEventPublisher;
import com.shabic.farm.domain.events.FarmCreated;
import com.shabic.farm.domain.model.aggregate.Farm;
import com.shabic.farm.domain.model.valueobject.GeoLocation;
import com.shabic.farm.domain.repository.FarmRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FarmServiceTest {

	@Mock private FarmRepository farmRepo;
	@Mock private FarmEventPublisher farmEventPublisher;

	@Captor private ArgumentCaptor<Farm> farmCaptor;
	@Captor private ArgumentCaptor<FarmCreated> farmCreatedCaptor;

	private FarmService service;

	@BeforeEach
	void setUp() {
		service = new FarmService(farmRepo, farmEventPublisher);
	}

	@Test
	void register_savesNewFarm_andReturnsId() {
		RegisterFarmCommand cmd = new RegisterFarmCommand(
				"Green Valley",
				"Riyadh",
				"King Fahd Road",
				24.7136,
				46.6753,
				"farm@example.com",
				"  REG-001  ",
				"+966501234567"
		);
		when(farmRepo.findByRegisterId("REG-001")).thenReturn(Optional.empty());

		UUID id = service.register(cmd);

		verify(farmRepo).save(farmCaptor.capture());
		Farm saved = farmCaptor.getValue();
		assertThat(id).isEqualTo(saved.getId());
		assertThat(saved.getName()).isEqualTo("Green Valley");
		assertThat(saved.getRegion()).isEqualTo("Riyadh");
		assertThat(saved.getRegisterId()).isEqualTo("REG-001");
		assertThat(saved.getLocation()).isEqualTo(new GeoLocation(24.7136, 46.6753));

		verify(farmEventPublisher).publishFarmCreated(farmCreatedCaptor.capture());
		FarmCreated published = farmCreatedCaptor.getValue();
		assertThat(published.farmId()).isEqualTo(id);
		assertThat(published.name()).isEqualTo("Green Valley");
		assertThat(published.eventType()).isEqualTo("FarmCreated");
	}

	@Test
	void register_rejectsDuplicateRegisterId_afterNormalization() {
		RegisterFarmCommand cmd = minimalCommand("  REG-001  ");
		when(farmRepo.findByRegisterId("REG-001")).thenReturn(Optional.of(existingFarm(UUID.randomUUID(), "REG-001")));

		assertThatThrownBy(() -> service.register(cmd))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("registerId already exists");

		verify(farmRepo, never()).save(any());
		verify(farmEventPublisher, never()).publishFarmCreated(any());
	}

	@Test
	void register_rejectsPartialLocation() {
		RegisterFarmCommand cmd = new RegisterFarmCommand(
				"Farm",
				null,
				null,
				24.0,
				null,
				null,
				null,
				null
		);

		assertThatThrownBy(() -> service.register(cmd))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("location requires both latitude and longitude");
	}

	@Test
	void update_rejectsWhenFarmNotFound() {
		UUID farmId = UUID.randomUUID();
		UpdateFarmCommand cmd = new UpdateFarmCommand(farmId, minimalCommand(null));
		when(farmRepo.findById(farmId)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.update(cmd))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("farm not found");
	}

	@Test
	void update_rejectsDuplicateRegisterId_ownedByAnotherFarm() {
		UUID farmId = UUID.randomUUID();
		UUID otherId = UUID.randomUUID();

		when(farmRepo.findById(farmId)).thenReturn(Optional.of(existingFarm(farmId, "REG-1")));
		when(farmRepo.findByRegisterId("REG-2")).thenReturn(Optional.of(existingFarm(otherId, "REG-2")));

		UpdateFarmCommand cmd = new UpdateFarmCommand(farmId, minimalCommand("REG-2"));

		assertThatThrownBy(() -> service.update(cmd))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("registerId already exists");
		verify(farmRepo, never()).save(any());
	}

	@Test
	void update_savesRehydratedFarm_preservingCreatedAt() {
		UUID farmId = UUID.randomUUID();
		Instant createdAt = Instant.parse("2024-01-01T00:00:00Z");
		Farm existing = Farm.rehydrate(
				farmId,
				"Old Name",
				"Old Region",
				null,
				null,
				null,
				"REG-1",
				null,
				createdAt
		);
		when(farmRepo.findById(farmId)).thenReturn(Optional.of(existing));
		when(farmRepo.findByRegisterId("REG-2")).thenReturn(Optional.empty());

		RegisterFarmCommand details = new RegisterFarmCommand(
				"  New Name  ",
				"  New Region  ",
				"  New Address  ",
				10.0,
				20.0,
				"  new@example.com  ",
				"  REG-2  ",
				"  +123  "
		);
		UpdateFarmCommand cmd = new UpdateFarmCommand(farmId, details);

		Farm updated = service.update(cmd);

		verify(farmRepo).save(farmCaptor.capture());
		Farm saved = farmCaptor.getValue();
		assertThat(updated).isEqualTo(saved);
		assertThat(saved.getName()).isEqualTo("New Name");
		assertThat(saved.getRegion()).isEqualTo("New Region");
		assertThat(saved.getRegisterId()).isEqualTo("REG-2");
		assertThat(saved.getCreatedAt()).isEqualTo(createdAt);
	}

	@Test
	void delete_removesFarm_whenExists() {
		UUID farmId = UUID.randomUUID();
		when(farmRepo.findById(farmId)).thenReturn(Optional.of(existingFarm(farmId, null)));

		service.delete(farmId);

		verify(farmRepo).deleteById(farmId);
	}

	@Test
	void delete_throwsWhenFarmNotFound() {
		UUID farmId = UUID.randomUUID();
		when(farmRepo.findById(farmId)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.delete(farmId))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("farm not found");

		verify(farmRepo, never()).deleteById(any());
	}

	@Test
	void getDetails_returnsFarm_orThrows() {
		UUID id = UUID.randomUUID();
		Farm existing = existingFarm(id, "REG-1");
		when(farmRepo.findById(id)).thenReturn(Optional.of(existing));

		assertThat(service.getDetails(id)).isSameAs(existing);

		when(farmRepo.findById(id)).thenReturn(Optional.empty());
		assertThatThrownBy(() -> service.getDetails(id))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("farm not found");
	}

	@Test
	void getAll_delegatesToRepository() {
		List<Farm> farms = List.of(existingFarm(UUID.randomUUID(), null));
		when(farmRepo.findAll()).thenReturn(farms);

		assertThat(service.getAll()).isSameAs(farms);
	}

	private static RegisterFarmCommand minimalCommand(String registerId) {
		return new RegisterFarmCommand(
				"Farm Name",
				null,
				null,
				null,
				null,
				null,
				registerId,
				null
		);
	}

	private static Farm existingFarm(UUID farmId, String registerId) {
		return Farm.rehydrate(
				farmId,
				"Farm",
				null,
				null,
				null,
				null,
				registerId,
				null,
				Instant.parse("2024-01-01T00:00:00Z")
		);
	}
}
