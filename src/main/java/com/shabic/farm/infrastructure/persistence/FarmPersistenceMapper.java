package com.shabic.farm.infrastructure.persistence;

import com.shabic.farm.domain.model.aggregate.Farm;
import com.shabic.farm.domain.model.valueobject.GeoLocation;
import com.shabic.farm.infrastructure.persistence.entities.FarmEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface FarmPersistenceMapper {
	@Mapping(target = "locationLatitude", source = "location", qualifiedByName = "latitude")
	@Mapping(target = "locationLongitude", source = "location", qualifiedByName = "longitude")
	FarmEntity toEntity(Farm farm);

	default Farm toDomain(FarmEntity entity) {
		GeoLocation location = GeoLocation.ofNullable(entity.getLocationLatitude(), entity.getLocationLongitude());
		return Farm.rehydrate(
				entity.getId(),
				entity.getName(),
				entity.getRegion(),
				entity.getAddress(),
				location,
				entity.getEmail(),
				entity.getRegisterId(),
				entity.getPhoneNumber(),
				entity.getCreatedAt()
		);
	}

	@Named("latitude")
	static Double latitude(GeoLocation location) {
		return location == null ? null : location.latitude();
	}

	@Named("longitude")
	static Double longitude(GeoLocation location) {
		return location == null ? null : location.longitude();
	}
}
