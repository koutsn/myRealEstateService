package org.realEstate.myRealEstateService.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.myRealEstate.api.RegisterApi;
import org.myRealEstate.model.RegisterUserRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("${spring.application.name}")
public class RegisterController implements RegisterApi {

    @Override
    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@Valid RegisterUserRequest registerUserRequest) {
        return ResponseEntity.ok(null);
    }
}
