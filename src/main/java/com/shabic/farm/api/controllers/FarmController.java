package com.shabic.farm.api.controllers;

import com.shabic.farm.api.dto.FarmResponse;
import com.shabic.farm.api.dto.RegisterFarmRequest;
import com.shabic.farm.api.mappers.FarmApiMapper;
import com.shabic.farm.api.mappers.FarmCommandMapper;
import com.shabic.farm.application.service.FarmService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/farms")
@RequiredArgsConstructor
public class FarmController {
	private final FarmService farmService;
	private final FarmCommandMapper commandMapper;
	private final FarmApiMapper apiMapper;

	@PostMapping
	@PreAuthorize("hasAnyRole('FARM_USER', 'FARM_ADMIN', 'ADMIN')")
	@ResponseStatus(HttpStatus.CREATED)
	public UUID register(@Valid @RequestBody RegisterFarmRequest request) {
		return farmService.register(commandMapper.toCommand(request));
	}

	@PutMapping("/{id}")
	@PreAuthorize("hasAnyRole('FARM_USER', 'FARM_ADMIN', 'ADMIN')")
	public FarmResponse update(@PathVariable("id") UUID farmId, @Valid @RequestBody RegisterFarmRequest request) {
		return apiMapper.toResponse(farmService.update(commandMapper.toUpdateCommand(farmId, request)));
	}

	@GetMapping
	@PreAuthorize("hasAnyRole('ADMIN', 'FARM_ADMIN', 'FARM_USER')")
	public List<FarmResponse> getAll() {
		return farmService.getAll()
				.stream()
				.map(apiMapper::toResponse)
				.toList();
	}

	@GetMapping("/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'FARM_ADMIN', 'FARM_USER')")
	public FarmResponse getDetails(@PathVariable("id") UUID farmId) {
		return apiMapper.toResponse(farmService.getDetails(farmId));
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("hasAnyRole('FARM_ADMIN', 'ADMIN')")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") UUID farmId) {
		farmService.delete(farmId);
	}
}
