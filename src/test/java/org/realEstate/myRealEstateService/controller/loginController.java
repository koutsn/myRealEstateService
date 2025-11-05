package org.realEstate.myRealEstateService.controller;

import org.myRealEstate.api.LoginApi;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class loginController implements LoginApi {

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        // Replace with real authentication logic / service
        if ("user".equals(username) && "pass".equals(password)) {
            Map<String, String> body = new HashMap<>();
            body.put("token", "mock-token-12345");
            return ResponseEntity.ok(body);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
