package org.realEstate.myRealEstateService.service;

import lombok.RequiredArgsConstructor;
import org.realEstate.myRealEstateService.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoginService {

    @Autowired
    private JwtUtil jwtUtil;

    public String login(String username, String password) {
        if ("test_user".equals(username) && "password".equals(password)) {
            List<String> roles = List.of("USER");

            String token = jwtUtil.generateToken(username, roles);

            String user = jwtUtil.getUsername(token);

            List<String> user_roles = jwtUtil.getRoles(token);

            return token;
        }
        return null;
    }

}