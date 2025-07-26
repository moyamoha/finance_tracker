package com.finance_tracker.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance_tracker.dto.responses.user.ProfileResponse;
import com.finance_tracker.entity.User;
import com.finance_tracker.exception.authentication.InvalidOrExpiredToken;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    @Value("${JWT_SECRET_KEY}")
    private String SECRET_KEY;

    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7; // 1 week

    @Autowired
    private ObjectMapper objectMapper;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateToken(ProfileResponse profile) {
        return Jwts.builder()
                .setSubject(profile.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .claim("payload", profile)
                .compact();
    }

    public String extractEmail(String token) {
        try {
            return this.parseClaims(token).getSubject();
        } catch (IllegalArgumentException | JwtException e) {
            throw new InvalidOrExpiredToken();
        }
    }

    public boolean isTokenValid(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateTokenForUser(User user) {
        ProfileResponse prof = new ProfileResponse(
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.isMfaEnabled(),
                user.getCurrency(),
                user.getTimezone(),
                user.getJoinedAt().toString()
        );
        return generateToken(prof);
    }
}
