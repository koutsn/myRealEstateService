package org.realEstate.myRealEstateService.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.myRealEstate.model.LoginUser200Response;
import org.realEstate.myRealEstateService.dto.LoginDto;
import org.realEstate.myRealEstateService.dto.UserDto;
import org.realEstate.myRealEstateService.exception.UnauthorizedException;
import org.realEstate.myRealEstateService.response.ErrorResponse;
import org.realEstate.myRealEstateService.service.UserService;
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
public class userController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity loginUser(@Valid @RequestBody LoginDto loginDto) {

        String token;
        try {
            token = userService.login(loginDto.getUsername(), loginDto.getPassword());
            LoginUser200Response loginUser200Response = new LoginUser200Response();
            loginUser200Response.setToken(token);
            return ResponseEntity.ok(loginUser200Response);
        } catch (UnauthorizedException e) {
            ErrorResponse response = new ErrorResponse(HttpStatus.UNAUTHORIZED.toString(), e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(response);
        } catch (Exception e) {
            ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }

    }

    @PostMapping("/register")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity registerUser(@Valid @RequestBody UserDto userDto) {

        try {
            userService.registerUser(userDto);
            return ResponseEntity.created(null).build();
        } catch (Exception e) {
            ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }
    }
}
