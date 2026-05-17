package com.shabic.farm.api.mappers;

import com.shabic.farm.api.dto.GeoLocationDto;
import com.shabic.farm.api.dto.RegisterFarmRequest;
import com.shabic.farm.application.command.RegisterFarmCommand;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-17T16:33:16+0300",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-9.4.1.jar, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class FarmCommandMapperImpl implements FarmCommandMapper {

    @Override
    public RegisterFarmCommand toCommand(RegisterFarmRequest request) {
        if ( request == null ) {
            return null;
        }

        Double locationLatitude = null;
        Double locationLongitude = null;
        String name = null;
        String region = null;
        String address = null;
        String email = null;
        String registerId = null;
        String phoneNumber = null;

        locationLatitude = requestLocationLatitude( request );
        locationLongitude = requestLocationLongitude( request );
        name = request.getName();
        region = request.getRegion();
        address = request.getAddress();
        email = request.getEmail();
        registerId = request.getRegisterId();
        phoneNumber = request.getPhoneNumber();

        RegisterFarmCommand registerFarmCommand = new RegisterFarmCommand( name, region, address, locationLatitude, locationLongitude, email, registerId, phoneNumber );

        return registerFarmCommand;
    }

    private Double requestLocationLatitude(RegisterFarmRequest registerFarmRequest) {
        GeoLocationDto location = registerFarmRequest.getLocation();
        if ( location == null ) {
            return null;
        }
        return location.getLatitude();
    }

    private Double requestLocationLongitude(RegisterFarmRequest registerFarmRequest) {
        GeoLocationDto location = registerFarmRequest.getLocation();
        if ( location == null ) {
            return null;
        }
        return location.getLongitude();
    }
}
