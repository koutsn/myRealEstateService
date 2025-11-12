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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${spring.application.name}")
public class userController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginDto loginDto) {

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
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDto userDto) {

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

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/user/{username}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> registerUser(@PathVariable String username) {
        try {
            UserDto user = userService.getUserByUsername(username);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }
    }

    @DeleteMapping("/user/{username}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable String username) {
        try {
            userService.deleteUserByUsername(username);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }
    }
}
