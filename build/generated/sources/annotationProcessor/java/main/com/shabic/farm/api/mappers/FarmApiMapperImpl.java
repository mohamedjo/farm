package com.shabic.farm.api.mappers;

import com.shabic.farm.api.dto.FarmResponse;
import com.shabic.farm.domain.model.aggregate.Farm;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-22T23:33:41+0300",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-9.4.1.jar, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class FarmApiMapperImpl implements FarmApiMapper {

    @Override
    public FarmResponse toResponse(Farm farm) {
        if ( farm == null ) {
            return null;
        }

        FarmResponse.FarmResponseBuilder farmResponse = FarmResponse.builder();

        farmResponse.location( toLocationDto( farm.getLocation() ) );
        farmResponse.id( farm.getId() );
        farmResponse.name( farm.getName() );
        farmResponse.region( farm.getRegion() );
        farmResponse.address( farm.getAddress() );
        farmResponse.email( farm.getEmail() );
        farmResponse.registerId( farm.getRegisterId() );
        farmResponse.phoneNumber( farm.getPhoneNumber() );
        farmResponse.numberOfAnimals( farm.getNumberOfAnimals() );
        farmResponse.createdAt( farm.getCreatedAt() );

        return farmResponse.build();
    }
}
