package com.sesac7.hellopet.common.utils;

import com.sesac7.hellopet.domain.user.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
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

    private ResponseCookie buildCookie(
            String name,
            String value,
            long maxAgeMinutes
    ) {
        return ResponseCookie.from(name, value)
                             .httpOnly(true)
                             .secure(true)
                             .path("/")
                             .sameSite("Strict")
                             .maxAge(Duration.ofMinutes(maxAgeMinutes))
                             .build();
    }

    public ResponseCookie generateAccessCookie(CustomUserDetails userDetails) {
        String token = doGenerateToken(userDetails, expiration);
        return buildCookie("accessToken", token, expiration);
    }

    public ResponseCookie generateAccessCookie(User user) {
        String token = doGenerateToken(user, expiration);
        return buildCookie("accessToken", token, expiration);
    }

    public ResponseCookie generateRefreshCookie(CustomUserDetails userDetails) {
        String token = doGenerateToken(userDetails, refreshExpiration);
        return buildCookie("refreshToken", token, refreshExpiration);
    }

    public ResponseCookie deleteAccessCookie() {
        return buildCookie("accessToken", "", 0);
    }

    public ResponseCookie deleteRefreshCookie() {
        return buildCookie("refreshToken", "", 0);
    }

    private String doGenerateToken(CustomUserDetails userDetails, Long expiration) {
        Claims claims = Jwts.claims();
        claims.setSubject(userDetails.getUsername());
        return Jwts.builder()
                   .setClaims(claims)
                   .claim("role", userDetails.getRole().name())
                   .setIssuedAt(new Date(System.currentTimeMillis()))
                   .setExpiration(new Date(System.currentTimeMillis() + expiration * 60 * 1000))
                   .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                   .compact();
    }

    private String doGenerateToken(User user, Long expiration) {
        Claims claims = Jwts.claims();
        claims.setSubject(user.getEmail());
        return Jwts.builder()
                   .setClaims(claims)
                   .claim("role", user.getRole().name())
                   .setIssuedAt(new Date(System.currentTimeMillis()))
                   .setExpiration(new Date(System.currentTimeMillis() + expiration * 60 * 1000))
                   .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                   .compact();
    }

    public String getEmailFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();
    }

    public Boolean validateToken(String token, CustomUserDetails userDetails) {
        String email = getEmailFromToken(token);
        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public Boolean isTokenExpired(String token) {
        final Date expirationDate = getExpirationDateFromToken(token);
        return expirationDate.before(new Date());
    }

    private Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }
}
