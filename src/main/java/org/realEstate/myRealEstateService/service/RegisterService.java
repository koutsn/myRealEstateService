package org.realEstate.myRealEstateService.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.realEstate.myRealEstateService.dto.UserDto;
import org.realEstate.myRealEstateService.mapper.UserMapper;
import org.realEstate.myRealEstateService.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class RegisterService {
    
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public void registerUser(UserDto userDto) {

        //UserEntity userEntity = userMapper.requestToEntity(request);
        //userRepository.save(userEntity);

    }
}
