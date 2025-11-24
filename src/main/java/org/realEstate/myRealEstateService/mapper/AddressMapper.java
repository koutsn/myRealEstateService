package org.realEstate.myRealEstateService.mapper;

import org.mapstruct.Mapper;
import org.realEstate.myRealEstateService.dto.AddressDto;
import org.realEstate.myRealEstateService.entity.AddressEntity;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    AddressEntity toEntity(AddressDto dto);

    AddressDto toDto(AddressEntity entity);
}
