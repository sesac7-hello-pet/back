package com.sesac7.hellopet.common.utils;

import com.sesac7.hellopet.domain.user.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;
import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

/**
 * jwt 토큰을 만들거나 파싱하기 위하여 만들었습니다.
 */
@Component
public class JwtUtil {

    @Value("${jwt.security}")
    private String secret;

    @Value("${jwt.access-token-expiration-ms}")
    private Long expiration;

    @Value("${jwt.refresh-token-expiration-ms}")
    private Long refreshExpiration;

    /**
     * claim 객체를 사용하거나 할때
     * 들어가게 되는 Key를 만들기 위한 메서드 입니다.
     * 보통 Value 어노테이션으로 들고와서 Kyes클래스를 활용하여 변환합니다.
     *
     * @return
     */
    private Key getSigningKey() {
        byte[] keyBytes = secret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 쿠키를 만들기 위한 공통 메서드 입니다.
     * name, value, maxAge 등으로 구분하여
     * accessToken, refreshToken, deleteToken을 쿠키로 만들 수 있습니다.
     *
     * @param name
     * @param value
     * @param maxAgeMinutes
     * @return
     */
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

    /**
     * 쿠키에 쓰일 토큰을 만드는 공통 메서드입니다.
     * jwt 토큰은 claims라는 방식을 씁니다.
     *
     * @param userDetails
     * @param expiration
     * @return
     */
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

    /**
     * 위와 동일한 메서드인데 user를 쓰느냐 userDetails를 쓰느냐의 차이 입니다.
     *
     * @param user
     * @param expiration
     * @return
     */
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

    /**
     * token에서 email을 추출합니다
     *
     * @param token
     * @return
     */
    public String getEmailFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * 토큰에서 어떤 것을 가져오고 싶을 때 쓰이는 공통 메서드 입니다.
     * claimsResolver자리에 getSubject, getExpiration등으로 다른 결과물을 만들 수 있습니다.
     *
     * @param token
     * @param claimsResolver
     * @param <T>
     * @return
     */
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

    /**
     * request에서 name과 동일한 쿠키가 있는지 확인하는 메서드 입니다.
     * 쿠키는 보통 reqeust와 같이 오기 때문에 이렇게 만들었습니다.
     *
     * @param req
     * @param name
     * @return
     */
    public String findCookie(HttpServletRequest req, String name) {
        return Arrays.stream(Optional.ofNullable(req.getCookies()).orElse(new Cookie[0]))
                     .filter(c -> name.equals(c.getName()))
                     .map(Cookie::getValue)
                     .findFirst()
                     .orElse(null);
    }
}
