package org.realEstate.myRealEstateService.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    public String login(String username, String password) {
        return "dummy-token";
    }

}
