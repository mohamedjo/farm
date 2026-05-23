package com.shabic.farm.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FarmResponse {
	private UUID id;
	private String name;
	private String region;
	private String address;
	private GeoLocationDto location;
	private String email;
	private String registerId;
	private String phoneNumber;
	private int numberOfAnimals;
	private Instant createdAt;
}
