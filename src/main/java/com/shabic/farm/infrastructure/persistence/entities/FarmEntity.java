package com.shabic.farm.infrastructure.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "farm")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FarmEntity {
	@Id
	@Column(name = "id", nullable = false)
	private UUID id;

	@Column(name = "name", nullable = false, length = 255)
	private String name;

	@Column(name = "region", length = 128)
	private String region;

	@Column(name = "address", length = 512)
	private String address;

	@Column(name = "location_latitude")
	private Double locationLatitude;

	@Column(name = "location_longitude")
	private Double locationLongitude;

	@Column(name = "email", length = 255)
	private String email;

	@Column(name = "register_id", unique = true, length = 128)
	private String registerId;

	@Column(name = "phone_number", length = 64)
	private String phoneNumber;

	@Column(name = "number_of_animals", nullable = false)
	private int numberOfAnimals;

	@Column(name = "created_at", nullable = false)
	private Instant createdAt;
}
