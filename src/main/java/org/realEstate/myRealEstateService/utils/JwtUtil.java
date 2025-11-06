package org.realEstate.myRealEstateService.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    // Secret key for signing the JWT (keep it safe!)
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Token validity in milliseconds (e.g., 1 hour)
    private final long jwtExpirationMs = 3600000;

    // Generate JWT token
    public String generateToken(String username, List<String> roles) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .claim("roles", roles)
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key)
                .compact();
    }

    // Extract username from JWT
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(String.valueOf(key))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
