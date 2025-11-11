package org.realEstate.myRealEstateService.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    private final String user = "testUser";
    private final List<String> roles = new ArrayList<>();

    @BeforeEach
    void setup() {
        roles.add("TestRole");
    }

    @Test
    void validateToken() {
        String token = jwtUtil.generateToken(user, roles);
        assertNotNull(token);
        assertTrue(jwtUtil.validateToken(token));
        assertEquals(user, jwtUtil.getUsername(token));
        assertEquals(roles, jwtUtil.getRoles(token));
    }

    @Test
    void validateInvalidToken() {
        String token = "INvalidToken";
        assertNotNull(token);
        assertFalse(jwtUtil.validateToken(token));
        assertNull(jwtUtil.getUsername(token));
        assertNull(jwtUtil.getRoles(token));
    }

}