package org.realEstate.myRealEstateService.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.myRealEstate.model.RegisterUserRequest;
import org.realEstate.myRealEstateService.dto.UserDto;
import org.realEstate.myRealEstateService.entity.UserEntity;
import org.realEstate.myRealEstateService.utils.Encrypt;;

@Mapper(componentModel = "spring", imports = Encrypt.class)
public interface UserMapper {

    UserMapper mapper = Mappers.getMapper(UserMapper.class);

    UserDto userToUserDto(UserEntity userEntity);

    UserEntity userDtoToUser(UserDto userDto);

    @Mapping(target = "username", source = "request.username")
    @Mapping(target = "password", expression = "java(Encrypt.encryptPassword(request.getPassword()))")
    @Mapping(target = "email", source = "request.email")
    @Mapping(target = "lastName", source = "request.lastName")
    @Mapping(target = "firstName", source = "request.firstName")
    @Mapping(target = "role", expression = "java(request.getRole().getValue())")
    @Mapping(target = "status", expression = "java(request.getStatus().getValue())")
    UserEntity requestToEntity(RegisterUserRequest request);

}
