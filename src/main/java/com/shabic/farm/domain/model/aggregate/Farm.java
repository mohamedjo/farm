package com.shabic.farm.domain.model.aggregate;

import com.shabic.farm.domain.model.valueobject.GeoLocation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class Farm {
	@EqualsAndHashCode.Include
	private final UUID id;
	private final String name;
	private final String region;
	private final String address;
	private final GeoLocation location;
	private final String email;
	private final String registerId;
	private final String phoneNumber;
	private final Instant createdAt;

	private static String blankToNull(String value) {
		if (value == null || value.isBlank()) {
			return null;
		}
		return value.trim();
	}

	private static Farm create(
			UUID id,
			String name,
			String region,
			String address,
			GeoLocation location,
			String email,
			String registerId,
			String phoneNumber,
			Instant createdAt
	) {
		Objects.requireNonNull(id, "id");
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException("name is required");
		}
		Objects.requireNonNull(createdAt, "createdAt");
		return new Farm(
				id,
				name.trim(),
				blankToNull(region),
				blankToNull(address),
				location,
				blankToNull(email),
				blankToNull(registerId),
				blankToNull(phoneNumber),
				createdAt
		);
	}

	public static Farm register(
			UUID id,
			String name,
			String region,
			String address,
			GeoLocation location,
			String email,
			String registerId,
			String phoneNumber,
			Instant now
	) {
		return create(id, name, region, address, location, email, registerId, phoneNumber, now);
	}

	public static Farm rehydrate(
			UUID id,
			String name,
			String region,
			String address,
			GeoLocation location,
			String email,
			String registerId,
			String phoneNumber,
			Instant createdAt
	) {
		return create(id, name, region, address, location, email, registerId, phoneNumber, createdAt);
	}
}
