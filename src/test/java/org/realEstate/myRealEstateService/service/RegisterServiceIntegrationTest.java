package org.realEstate.myRealEstateService.service;

import org.junit.jupiter.api.Test;
import org.myRealEstate.model.RegisterUserRequest;
import org.realEstate.myRealEstateService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RegisterServiceIntegrationTest {

    @Autowired
    RegisterService registerService;

    @Autowired
    UserRepository userRepository;

    @Test
    void registerUser() {
        RegisterUserRequest registerUserRequest = new RegisterUserRequest();
        registerUserRequest.setUsername("username");
        registerUserRequest.setPassword("password");
        registerUserRequest.setEmail("email@yahoo.com");
        registerUserRequest.setFirstName("firstName");
        registerUserRequest.setLastName("lastName");
        registerUserRequest.setRole(RegisterUserRequest.RoleEnum.USER);
        registerUserRequest.setStatus(RegisterUserRequest.StatusEnum.ACTIVE);
        registerService.registerUser(registerUserRequest);
    }

}