package org.realEstate.myRealEstateService.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.realEstate.myRealEstateService.dto.UserDto;
import org.realEstate.myRealEstateService.entity.UserEntity;
import org.realEstate.myRealEstateService.utils.Encrypt;

@Mapper(componentModel = "spring", imports = Encrypt.class)
public interface UserMapper {

    UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "password", expression = "java(Encrypt.encryptPassword(userDto.getPassword()))")
    UserEntity toEntity(UserDto userDto);

    @Mapping(target = "password", ignore = true)
    UserDto toDto(UserEntity userEntity);

}
