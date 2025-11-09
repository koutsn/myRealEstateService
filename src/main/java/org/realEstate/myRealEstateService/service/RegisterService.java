package org.realEstate.myRealEstateService.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.myRealEstate.model.RegisterUserRequest;
import org.realEstate.myRealEstateService.entity.UserEntity;
import org.realEstate.myRealEstateService.mapper.UserMapper;
import org.realEstate.myRealEstateService.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class RegisterService {
    
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public void registerUser(RegisterUserRequest request) {

        UserEntity userEntity = userMapper.requestToEntity(request);
        userRepository.save(userEntity);

    }
}
