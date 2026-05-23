package com.shabic.farm.infrastructure.persistence;

import com.shabic.farm.domain.model.aggregate.Farm;
import com.shabic.farm.infrastructure.persistence.entities.FarmEntity;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-22T23:33:41+0300",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-9.4.1.jar, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class FarmPersistenceMapperImpl implements FarmPersistenceMapper {

    @Override
    public FarmEntity toEntity(Farm farm) {
        if ( farm == null ) {
            return null;
        }

        FarmEntity.FarmEntityBuilder farmEntity = FarmEntity.builder();

        farmEntity.locationLatitude( FarmPersistenceMapper.latitude( farm.getLocation() ) );
        farmEntity.locationLongitude( FarmPersistenceMapper.longitude( farm.getLocation() ) );
        farmEntity.id( farm.getId() );
        farmEntity.name( farm.getName() );
        farmEntity.region( farm.getRegion() );
        farmEntity.address( farm.getAddress() );
        farmEntity.email( farm.getEmail() );
        farmEntity.registerId( farm.getRegisterId() );
        farmEntity.phoneNumber( farm.getPhoneNumber() );
        farmEntity.numberOfAnimals( farm.getNumberOfAnimals() );
        farmEntity.createdAt( farm.getCreatedAt() );

        return farmEntity.build();
    }
}
