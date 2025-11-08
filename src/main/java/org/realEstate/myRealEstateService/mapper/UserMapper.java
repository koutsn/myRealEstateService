package org.realEstate.myRealEstateService.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.realEstate.myRealEstateService.dto.UserDto;
import org.realEstate.myRealEstateService.entity.UserEntity;

@Mapper
public interface UserMapper {

    UserMapper mapper = Mappers.getMapper(UserMapper.class);

    UserDto userToUserDto(UserEntity causer);

    UserEntity userDtoToUser(UserDto dto);
}
