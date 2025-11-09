package org.realEstate.myRealEstateService.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.myRealEstate.api.RegisterApi;
import org.myRealEstate.model.RegisterUserRequest;
import org.realEstate.myRealEstateService.service.RegisterService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("${spring.application.name}")
public class RegisterController implements RegisterApi {

    private final RegisterService registerService;

    @Override
    @PostMapping("/register")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> registerUser(@Valid RegisterUserRequest registerUserRequest) {

        registerService.registerUser(registerUserRequest);
        return ResponseEntity.ok(null);
    }
}
