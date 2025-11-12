package org.realEstate.myRealEstateService.service;

import lombok.RequiredArgsConstructor;
import org.realEstate.myRealEstateService.dto.UserDto;
import org.realEstate.myRealEstateService.entity.UserEntity;
import org.realEstate.myRealEstateService.exception.CustomException;
import org.realEstate.myRealEstateService.exception.UnauthorizedException;
import org.realEstate.myRealEstateService.mapper.UserMapper;
import org.realEstate.myRealEstateService.repository.UserRepository;
import org.realEstate.myRealEstateService.utils.Encrypt;
import org.realEstate.myRealEstateService.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    @Value("${super.password}")
    private String superUserPass;

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public String login(String username, String password) throws UnauthorizedException {

        List<String> roles = new ArrayList<>();
        if ("super".equals(username) && superUserPass.equals(password)) {
            roles.add("ADMIN");
            String token = jwtUtil.generateToken(username, roles);
            return token;
        }

        UserEntity user = userRepository.findByUsername(username).orElse(null);
        if (user != null && user.getPassword() != null && Encrypt.checkPassword(password, user.getPassword())) {
            roles.add(user.getRole());
            return jwtUtil.generateToken(user.getUsername(), roles);
        }
        throw new UnauthorizedException("Invalid username or password");
    }

    @Transient
    public void registerUser(UserDto userDto) throws CustomException {

        UserEntity userEntity = userRepository.findByUsername(userDto.getUsername()).orElse(null);
        if  (userEntity != null && userEntity.getUsername() != null)
        {
            throw new CustomException("User already exists");
        }

        UserEntity userEntitySave = userMapper.toEntity(userDto);
        userRepository.save(userEntitySave);

    }

    public  List<UserDto> getAllUsers() {
        List<UserEntity> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    public UserDto getUserByUsername(String username) throws CustomException {
        UserEntity userEntity = userRepository.findByUsername(username).orElse(null);
        if  (userEntity == null) {
            throw new CustomException("User does not exist");
        }
        return userMapper  .toDto(userEntity);
    }

    @Transient
    public void deleteUserByUsername(String username) throws CustomException
    {
        UserEntity user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            throw new CustomException("User does not exist");
        }
        userRepository.delete(user);
    }
}
