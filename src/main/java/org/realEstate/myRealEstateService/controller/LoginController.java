package org.realEstate.myRealEstateService.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.myRealEstate.model.LoginUser200Response;
import org.realEstate.myRealEstateService.dto.LoginDto;
import org.realEstate.myRealEstateService.response.ErrorResponse;
import org.realEstate.myRealEstateService.service.LoginService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${spring.application.name}")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity loginUser(@Valid @RequestBody LoginDto loginDto) {

        String token;
        try {
            token = loginService.login(loginDto.getUsername(), loginDto.getPassword());
            LoginUser200Response loginUser200Response = new LoginUser200Response();
            loginUser200Response.setToken(token);
            return ResponseEntity.ok(loginUser200Response);
        } catch (Exception e) {
            ErrorResponse response = new ErrorResponse(HttpStatus.UNAUTHORIZED.toString(), e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(response);
        }
    }
}
