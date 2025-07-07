package com.sesac7.hellopet.domain.auth.authService;

import com.sesac7.hellopet.common.utils.CustomUserDetails;
import com.sesac7.hellopet.common.utils.JwtUtil;
import com.sesac7.hellopet.domain.auth.dto.request.CheckPasswordRequest;
import com.sesac7.hellopet.domain.auth.dto.request.LoginRequest;
import com.sesac7.hellopet.domain.auth.dto.response.AuthResult;
import com.sesac7.hellopet.domain.auth.dto.response.CheckPasswordResponse;
import com.sesac7.hellopet.domain.auth.entity.RefreshToken;
import com.sesac7.hellopet.domain.auth.repository.RefreshTokenRepository;
import com.sesac7.hellopet.domain.user.entity.User;
import com.sesac7.hellopet.domain.user.service.UserFinder;
import com.sesac7.hellopet.domain.user.service.UserService;
import com.sesac7.hellopet.global.exception.custom.UnauthorizedException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final RefreshTokenRepository refreshTokenRepository;

    private final UserService userService;
    private final RefreshFinder refreshFinder;

    private final UserFinder userFinder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthResult userLogin(@Valid LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        ResponseCookie accessCookie = jwtUtil.generateAccessCookie(userDetails);
        ResponseCookie refreshCookie = jwtUtil.generateRefreshCookie(userDetails);
        User foundUser = userService.getUserByEmailFromDatabase(userDetails.getUsername());

        if (refreshFinder.existRefreshByUser(foundUser)) {
            refreshFinder.deleteRefreshByEmail(userDetails.getUsername());
        }

        refreshTokenRepository.save(new RefreshToken(null, refreshCookie.getValue(), foundUser));

        return new AuthResult(accessCookie, refreshCookie, userService.userLogin(userDetails.getUsername()));
    }

    public AuthResult userLogout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        User foundUser = userFinder.findLoggedInUserByUsername(userDetails.getUsername());

        refreshFinder.deleteRefreshByUser(foundUser);

        SecurityContextHolder.clearContext();
        return new AuthResult(jwtUtil.deleteAccessCookie(), jwtUtil.deleteRefreshCookie(), null);
    }

    public CheckPasswordResponse checkPassword(@Valid CheckPasswordRequest request, CustomUserDetails userDetails) {
        User loggedInUser = userFinder.findLoggedInUserByUsername(userDetails.getUsername());
        if (!userService.verifyLoggedInUserPassword(loggedInUser, request)) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }
        return new CheckPasswordResponse("확인 되었습니다.", true);
    }

    public ResponseCookie reissueAccess(HttpServletRequest request) {
        String token = Arrays.stream(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
                             .filter(c -> "refreshToken".equals(c.getName()))
                             .map(Cookie::getValue)
                             .findFirst().orElse(null);

        if (token == null) {
            throw new UnauthorizedException(jwtUtil.deleteAccessCookie(), jwtUtil.deleteRefreshCookie());
        }

        if (jwtUtil.isTokenExpired(token)) {
            throw new UnauthorizedException(jwtUtil.deleteAccessCookie(), jwtUtil.deleteRefreshCookie());
        }

        User foundUser;

        try {
            foundUser = refreshFinder.getUserByToken(token);
        } catch (NullPointerException e) {
            String email = jwtUtil.getEmailFromToken(token);
            refreshFinder.deleteRefreshByEmail(email);
            throw new UnauthorizedException(jwtUtil.deleteAccessCookie(), jwtUtil.deleteRefreshCookie());
        }

        return jwtUtil.generateAccessCookie(foundUser);
    }
}
