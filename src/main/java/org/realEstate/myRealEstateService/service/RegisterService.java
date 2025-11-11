package org.realEstate.myRealEstateService.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.realEstate.myRealEstateService.dto.UserDto;
import org.realEstate.myRealEstateService.entity.UserEntity;
import org.realEstate.myRealEstateService.exception.CustomException;
import org.realEstate.myRealEstateService.mapper.UserMapper;
import org.realEstate.myRealEstateService.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class RegisterService {
    
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public void registerUser(UserDto userDto) throws CustomException {

        UserEntity userEntity = userRepository.findByUsername(userDto.getUsername()).orElse(null);
        if  (userEntity != null && userEntity.getUsername() != null)
        {
            throw new CustomException("User already exists");
        }

        UserEntity userEntitySave = userMapper.toEntity(userDto);
        userRepository.save(userEntitySave);

    }
}
