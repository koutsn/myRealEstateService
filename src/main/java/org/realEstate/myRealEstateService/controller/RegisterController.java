package org.realEstate.myRealEstateService.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.myRealEstate.api.RegisterApi;
import org.myRealEstate.model.RegisterUserRequest;
import org.realEstate.myRealEstateService.dto.UserDto;
import org.realEstate.myRealEstateService.exception.CustomException;
import org.realEstate.myRealEstateService.exception.UnauthorizedException;
import org.realEstate.myRealEstateService.response.ErrorResponse;
import org.realEstate.myRealEstateService.service.RegisterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("${spring.application.name}")
public class RegisterController {

    private final RegisterService registerService;

    @PostMapping("/register")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity registerUser(@Valid @RequestBody UserDto userDto) {

        try {
            registerService.registerUser(userDto);
            return ResponseEntity.created(null).build();
        } catch (Exception e) {
            ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }
    }
}
