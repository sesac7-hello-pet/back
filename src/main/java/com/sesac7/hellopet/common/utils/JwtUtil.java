package com.sesac7.hellopet.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Duration;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    @Value("${jwt.security}")
    private String secret;

    @Value("${jwt.access-token-expiration-ms}")
    private Long expiration;

    @Value("${jwt.refresh-token-expiration-ms}")
    private Long refreshExpiration;

    private Key getSigningKey() {
        byte[] keyBytes = secret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public ResponseCookie generateCookie(UserDetails userDetails) {
        String token = doGenerateToken(userDetails, expiration);
        return ResponseCookie.from("accessToken", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofMinutes(expiration))
                .sameSite("Strict")
                .build();
    }

    public ResponseCookie generateRefreshCookie(UserDetails userDetails) {
        String token = doGenerateToken(userDetails, refreshExpiration);
        return ResponseCookie.from("refreshToken", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(Duration.ofMinutes(refreshExpiration))
                .sameSite("Strict")
                .build();
    }

    private String doGenerateToken(UserDetails userDetails, Long expiration) {
        Claims claims = Jwts.claims();
        claims.setSubject(userDetails.getUsername());
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 60 * 1000))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}
