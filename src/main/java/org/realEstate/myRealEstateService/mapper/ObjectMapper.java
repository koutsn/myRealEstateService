package org.realEstate.myRealEstateService.mapper;

import org.mapstruct.Mapper;
import org.realEstate.myRealEstateService.dto.ObjecFilesDto;
import org.realEstate.myRealEstateService.dto.ObjectDto;
import org.realEstate.myRealEstateService.entity.ObjectEntity;
import org.realEstate.myRealEstateService.entity.ObjectFilesEntity;

@Mapper(componentModel = "spring", uses = { AddressMapper.class })
public interface ObjectMapper {
    ObjectEntity toEntity(ObjectDto objectDto);

    ObjectDto toDto(ObjectEntity objectEntity);
}
