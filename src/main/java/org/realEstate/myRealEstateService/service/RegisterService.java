package org.realEstate.myRealEstateService.service;

import lombok.RequiredArgsConstructor;
import org.myRealEstate.model.RegisterUserRequest;
import org.realEstate.myRealEstateService.repository.UserRepository;
import org.realEstate.myRealEstateService.utils.Encrypt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterService {

    private final Encrypt encrypt;
    private final UserRepository userRepository;


    public void registerUser(RegisterUserRequest request) {

        //UserEntity userRntity = userMapper.userDtoToUser(user);

    }
}
