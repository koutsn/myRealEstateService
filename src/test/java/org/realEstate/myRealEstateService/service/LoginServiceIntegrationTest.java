package org.realEstate.myRealEstateService.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.realEstate.myRealEstateService.dto.UserDto;
import org.realEstate.myRealEstateService.exception.UnauthorizedException;
import org.realEstate.myRealEstateService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.realEstate.myRealEstateService.Enum.Role.USER;
import static org.realEstate.myRealEstateService.Enum.Status.ACTIVE;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class LoginServiceIntegrationTest {

    @Autowired
    RegisterService registerService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    LoginService loginService;

    private String username = "username";
    private String password = "password";

    private String invalidPassword = "invsslidPassword";

    private String suoerUsername = "super";
    @Value("${super.password}")
    private String superPassword = "password";

    UserDto createUserDto() {
        UserDto userDto = new UserDto();
        userDto.setUsername(username);
        userDto.setPassword(password);
        userDto.setEmail("email@yahoo.com");
        userDto.setFirstName("firstName");
        userDto.setLastName("lastName");
        userDto.setRole(USER);
        userDto.setStatus(ACTIVE);
        return userDto;
    }

    @BeforeEach
    @SneakyThrows
    void setUp() {
        userRepository.deleteAll();
        UserDto userDto = createUserDto();
        registerService.registerUser(userDto);
    }

    @Test
    @SneakyThrows
    void loginUser() {
        String token = loginService.login(username, password);
        assertNotNull(token);
    }

    @Test
    @SneakyThrows
    void loginAsSuperUser() {
        String token = loginService.login(suoerUsername, superPassword);
        assertNotNull(token);
    }

    @Test
    void loginUserInvalidPassword() {
        Exception exception = assertThrows(
                UnauthorizedException.class,
                () -> {
                    loginService.login(username, invalidPassword);
                }
        );
        assertEquals("Invliad username or password", exception.getMessage());
    }

}