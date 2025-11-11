package org.realEstate.myRealEstateService.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.realEstate.myRealEstateService.dto.UserDto;
import org.realEstate.myRealEstateService.entity.UserEntity;
import org.realEstate.myRealEstateService.exception.CustomException;
import org.realEstate.myRealEstateService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.realEstate.myRealEstateService.Enum.Role.*;
import static org.realEstate.myRealEstateService.Enum.Status.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class RegisterServiceIntegrationTest {

    @Autowired
    RegisterService registerService;

    @Autowired
    UserRepository userRepository;

    private String username = "username";

    UserDto createUserDto() {
        UserDto userDto = new UserDto();
        userDto.setUsername(username);
        userDto.setPassword("password");
        userDto.setEmail("email@yahoo.com");
        userDto.setFirstName("firstName");
        userDto.setLastName("lastName");
        userDto.setRole(USER);
        userDto.setStatus(ACTIVE);
        return userDto;
    }

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @SneakyThrows
    void registerUser() {

        UserDto userDto = createUserDto();

        registerService.registerUser(userDto);

        Optional<UserEntity> user = userRepository.findByUsername(username);
        assertNotNull(user);
        assertEquals(1, user.stream().count());
        assertEquals(username, user.get().getUsername());
        assertEquals("email@yahoo.com", user.get().getEmail());
        assertEquals("firstName", user.get().getFirstName());
        assertEquals("lastName", user.get().getLastName());
        assertEquals("USER", user.get().getRole());
        assertEquals("ACTIVE", user.get().getStatus());
    }

    @Test
    void registerUser_shouldThrowWhenUserAlreadyExists() {
        registerUser();

        UserDto userDto = createUserDto();

        Exception exception = assertThrows(
                CustomException.class,
                () -> {
                    registerService.registerUser(userDto);
                }
        );
        assertEquals("User already exists", exception.getMessage());
    }
}