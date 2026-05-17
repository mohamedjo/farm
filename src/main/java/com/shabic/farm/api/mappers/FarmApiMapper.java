package com.shabic.farm.api.mappers;

import com.shabic.farm.api.dto.FarmResponse;
import com.shabic.farm.api.dto.GeoLocationDto;
import com.shabic.farm.domain.model.aggregate.Farm;
import com.shabic.farm.domain.model.valueobject.GeoLocation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FarmApiMapper {
	@Mapping(target = "location", source = "location")
	FarmResponse toResponse(Farm farm);

	default GeoLocationDto toLocationDto(GeoLocation location) {
		if (location == null) {
			return null;
		}
		return GeoLocationDto.builder()
				.latitude(location.latitude())
				.longitude(location.longitude())
				.build();
	}
}
