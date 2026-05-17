package com.shabic.farm.api.mappers;

import com.shabic.farm.api.dto.GeoLocationDto;
import com.shabic.farm.api.dto.RegisterFarmRequest;
import com.shabic.farm.application.command.RegisterFarmCommand;
import com.shabic.farm.application.command.UpdateFarmCommand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface FarmCommandMapper {

	@Mapping(target = "locationLatitude", source = "location.latitude")
	@Mapping(target = "locationLongitude", source = "location.longitude")
	RegisterFarmCommand toCommand(RegisterFarmRequest request);

	default UpdateFarmCommand toUpdateCommand(UUID farmId, RegisterFarmRequest request) {
		return new UpdateFarmCommand(farmId, toCommand(request));
	}
}
