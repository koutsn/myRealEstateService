package org.realEstate.myRealEstateService.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.myRealEstate.api.LoginApi;
import org.myRealEstate.model.LoginUser200Response;
import org.myRealEstate.model.LoginUserRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("${spring.application.name}")
public class LoginController implements LoginApi {

    @Override
    @PostMapping("/login")
    public ResponseEntity<LoginUser200Response> loginUser(@Valid LoginUserRequest loginUserRequest) {

        LoginUser200Response loginUser200Response = new LoginUser200Response();
        loginUser200Response.setToken("dummy-token");
        return ResponseEntity.ok(loginUser200Response);
    }
}
