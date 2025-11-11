package org.realEstate.myRealEstateService.service;

import lombok.RequiredArgsConstructor;
import org.realEstate.myRealEstateService.entity.UserEntity;
import org.realEstate.myRealEstateService.exception.CustomException;
import org.realEstate.myRealEstateService.exception.UnauthorizedException;
import org.realEstate.myRealEstateService.repository.UserRepository;
import org.realEstate.myRealEstateService.utils.Encrypt;
import org.realEstate.myRealEstateService.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoginService {

    @Value("${super.password}")
    private String userPass;

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public String login(String username, String password) throws UnauthorizedException {

        if ("super".equals(username) && userPass.equals(password)) {
            String token = jwtUtil.generateToken(username, "ADMIN");
            return token;
        }

        UserEntity user = userRepository.findByUsernameAndPassword(username, Encrypt.encryptPassword(password)).orElse(null);
        if (user != null && user.getUsername() != null && user.getRole() != null) {
            return jwtUtil.generateToken(user.getUsername(), user.getRole());
        }
        throw new UnauthorizedException("Invliad username or password");
    }

}