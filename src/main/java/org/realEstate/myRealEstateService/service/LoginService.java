package org.realEstate.myRealEstateService.service;

import lombok.RequiredArgsConstructor;
import org.realEstate.myRealEstateService.exception.CustomException;
import org.realEstate.myRealEstateService.utils.JwtUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final JwtUtil jwtUtil;

    public String login(String username, String password) throws CustomException {
        if ("test_user".equals(username) && "password".equals(password)) {
            List<String> roles = List.of("USER");
            String token = jwtUtil.generateToken(username, roles);
            return token;
        } else if ("test_user2".equals(username) && "password".equals(password)) {
            List<String> roles = List.of("ADMIN");
            String token = jwtUtil.generateToken(username, roles);
            return token;
        }
        throw new CustomException("Invliad username or password");
    }

}