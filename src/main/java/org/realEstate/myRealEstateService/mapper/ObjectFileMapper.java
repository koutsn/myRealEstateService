package org.realEstate.myRealEstateService.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.realEstate.myRealEstateService.dto.UserDto;
import org.realEstate.myRealEstateService.entity.UserEntity;
import org.realEstate.myRealEstateService.utils.Encrypt;

@Mapper(componentModel = "spring", imports = Encrypt.class)
public interface ObjectFileMapper {

    ObjectFileMapper mapper = Mappers.getMapper(ObjectFileMapper.class);

    UserEntity toEntity(UserDto userDto);

    UserDto toDto(UserEntity userEntity);
}
