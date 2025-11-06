package org.realEstate.myRealEstateService.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    public String login(String username, String password) {
        if ("user".equals(username) && "password".equals(password)) {
            return "valid-token";
        }
        return null;
    }

}