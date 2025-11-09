package org.realEstate.myRealEstateService.mapper;

import lombok.RequiredArgsConstructor;
import org.myRealEstate.model.RegisterUserRequest;
import org.realEstate.myRealEstateService.Enum.Role;
import org.realEstate.myRealEstateService.Enum.Status;
import org.realEstate.myRealEstateService.dto.UserDto;
import org.realEstate.myRealEstateService.entity.UserEntity;
import org.realEstate.myRealEstateService.utils.Encrypt;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class userMapperImpl implements UserMapper  {

    private final Encrypt encrypt;

    @Override
    public UserDto userToUserDto(UserEntity userEntity) {
        return null;
    }

    @Override
    public UserEntity userDtoToUser(UserDto userDto) {
        return null;
    }

    @Override
    public UserEntity requestToEntity(RegisterUserRequest request) {
        return UserEntity.builder()
                .username(request.getUsername())
                .password(encrypt.encryptPassword(request.getPassword()))
                .email(request.getEmail())
                .lastName(request.getLastName())
                .firstName(request.getFirstName())
                .roles(Role.valueOf(request.getRole().toString()))
                .status(Status.valueOf(request.getStatus().toString()))
                .build();
    }


}
