package org.realEstate.myRealEstateService.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.myRealEstate.model.RegisterUserRequest;
import org.realEstate.myRealEstateService.dto.UserDto;
import org.realEstate.myRealEstateService.entity.UserEntity;
import org.realEstate.myRealEstateService.repository.UserRepository;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper mapper = Mappers.getMapper(UserMapper.class);

    UserDto userToUserDto(UserEntity userEntity);

    UserEntity userDtoToUser(UserDto userDto);

    UserEntity requestToEntity(RegisterUserRequest request);
}
