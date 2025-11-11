package org.realEstate.myRealEstateService.service;

import lombok.RequiredArgsConstructor;
import org.realEstate.myRealEstateService.entity.UserEntity;
import org.realEstate.myRealEstateService.exception.UnauthorizedException;
import org.realEstate.myRealEstateService.repository.UserRepository;
import org.realEstate.myRealEstateService.utils.Encrypt;
import org.realEstate.myRealEstateService.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoginService {

    @Value("${super.password}")
    private String userPass;

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public String login(String username, String password) throws UnauthorizedException {

        List<String> roles = new ArrayList<>();
        if ("super".equals(username) && userPass.equals(password)) {
            roles.add("ADMIN");
            String token = jwtUtil.generateToken(username, roles);
            return token;
        }

        UserEntity user = userRepository.findByUsername(username).orElse(null);
        if (user != null && user.getPassword() != null && Encrypt.checkPassword(password, user.getPassword())) {
            roles.add(user.getRole());
            return jwtUtil.generateToken(user.getUsername(), roles);
        }
        throw new UnauthorizedException("Invliad username or password");
    }

}