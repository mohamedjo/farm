package com.shabic.farm.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterFarmRequest {
	@NotBlank
	private String name;

	private String region;

	private String address;

	private GeoLocationDto location;

	private String email;

	private String registerId;

	private String phoneNumber;
}
