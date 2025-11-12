package org.realEstate.myRealEstateService.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.realEstate.myRealEstateService.Enum.Role;
import org.realEstate.myRealEstateService.Enum.Status;
import org.realEstate.myRealEstateService.dto.UserDto;
import org.realEstate.myRealEstateService.entity.UserEntity;
import org.realEstate.myRealEstateService.exception.CustomException;
import org.realEstate.myRealEstateService.exception.UnauthorizedException;
import org.realEstate.myRealEstateService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.realEstate.myRealEstateService.Enum.Status.ACTIVE;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class UserServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    private String username = "username";
    private String password = "password";

    private String invalidPassword = "invsslidPassword";

    private String suoerUsername = "super";
    @Value("${super.password}")
    private String superPassword = "password";

    UserDto createUserDto(Role role, String username, String password, String email,
                          String firstName, String lastName, Status status) {
        UserDto userDto = new UserDto();
        userDto.setUsername(username);
        userDto.setPassword(password);
        userDto.setEmail(email);
        userDto.setFirstName(firstName);
        userDto.setLastName(lastName);
        userDto.setRole(role);
        userDto.setStatus(status);
        return userDto;
    }

    @BeforeEach
    @SneakyThrows
    void setUp() {
        userRepository.deleteAll();
        UserDto userDto = createUserDto(Role.USER, username, password, "email@yahoo.com", "firstName", "lastName", ACTIVE);
        userService.registerUser(userDto);
    }

    @SneakyThrows
    @ParameterizedTest
    @EnumSource(value = Role.class, names = {"USER", "MANAGER", "ADMIN"})
    void registerUser(Role role) {

        userRepository.deleteAll();

        UserDto userDto = createUserDto(role, username, password, "email@yahoo.com", "firstName", "lastName", ACTIVE);

        userService.registerUser(userDto);

        Optional<UserEntity> user = userRepository.findByUsername(username);
        assertNotNull(user);
        assertEquals(1, user.stream().count());
        assertEquals(username, user.get().getUsername());
        assertEquals("email@yahoo.com", user.get().getEmail());
        assertEquals("firstName", user.get().getFirstName());
        assertEquals("lastName", user.get().getLastName());
        assertEquals(role.toString(), user.get().getRole());
        assertEquals("ACTIVE", user.get().getStatus());
    }

    @Test
    void registerUser_shouldThrowWhenUserAlreadyExists() {
        registerUser(Role.USER);
        UserDto userDto = createUserDto(Role.USER, username, password, "email@yahoo.com", "firstName", "lastName", ACTIVE);
        Exception exception = assertThrows(
                CustomException.class,
                () -> {
                    userService.registerUser(userDto);
                }
        );
        assertEquals("User already exists", exception.getMessage());
    }

    @Test
    @SneakyThrows
    void loginUser() {
        String token = userService.login(username, password);
        assertNotNull(token);
    }

    @Test
    @SneakyThrows
    void loginAsSuperUser() {
        String token = userService.login(suoerUsername, superPassword);
        assertNotNull(token);
    }

    @Test
    void loginUserInvalidPassword() {
        Exception exception = assertThrows(
                UnauthorizedException.class,
                () -> {
                    userService.login(username, invalidPassword);
                }
        );
        assertEquals("Invalid username or password", exception.getMessage());
    }

    @Test
    @SneakyThrows
    void getAllUsers() {
        UserDto userDto = createUserDto(Role.ADMIN, username + "2", password, "email2@yahoo.com", "firstName2", "lastName2", ACTIVE);

        userService.registerUser(userDto);

        List<UserDto> userDtos = userService.getAllUsers();
        assertNotNull(userDtos);
        assertEquals(2, userDtos.stream().count());

        assertEquals("username", userDtos.get(0).getUsername());
        assertEquals("email@yahoo.com", userDtos.get(0).getEmail());
        assertEquals("firstName", userDtos.get(0).getFirstName());
        assertEquals("lastName", userDtos.get(0).getLastName());
        assertEquals(Role.USER, userDtos.get(0).getRole());
        assertEquals(ACTIVE, userDtos.get(0).getStatus());
    }

    @Test
    @SneakyThrows
    void getUserByUsername() {
        UserDto userDto = createUserDto(Role.ADMIN, username + "2", password, "email2@yahoo.com", "firstName2", "lastName2", ACTIVE);
        userService.registerUser(userDto);

        UserDto userDtos = userService.getUserByUsername(username);

        assertNotNull(userDtos);
        assertEquals("username", userDtos.getUsername());
        assertEquals("email@yahoo.com", userDtos.getEmail());
        assertEquals("firstName", userDtos.getFirstName());
        assertEquals("lastName", userDtos.getLastName());
        assertEquals(Role.USER, userDtos.getRole());
        assertEquals(ACTIVE, userDtos.getStatus());
    }

    @Test
    void getNotExistentUser() {
        Exception exception = assertThrows(
                CustomException.class,
                () -> {
                    userService.getUserByUsername("wrongUsername");
                }
        );
        assertEquals("User does not exist", exception.getMessage());
    }

    @Test
    @SneakyThrows
    void deleteUser() {
        List<UserEntity> user = userRepository.findAll();
        assertEquals(1, user.size());
        userService.deleteUserByUsername(username);
        List<UserEntity> deletedUser = userRepository.findAll();
        assertEquals(0, deletedUser.size());
    }

    @Test
    void deleteNonExistentUser() {
        Exception exception = assertThrows(
                CustomException.class,
                () -> {
                    userService.deleteUserByUsername("nonExistentUser");
                }
        );
        assertEquals("User does not exist", exception.getMessage());
    }
}
