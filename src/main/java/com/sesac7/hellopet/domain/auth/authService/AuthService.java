package com.sesac7.hellopet.domain.auth.authService;

import com.sesac7.hellopet.common.utils.CustomUserDetails;
import com.sesac7.hellopet.common.utils.JwtUtil;
import com.sesac7.hellopet.domain.auth.dto.request.CheckPasswordRequest;
import com.sesac7.hellopet.domain.auth.dto.request.LoginRequest;
import com.sesac7.hellopet.domain.auth.dto.response.AuthResult;
import com.sesac7.hellopet.domain.auth.dto.response.LoginResponse;
import com.sesac7.hellopet.domain.user.entity.User;
import com.sesac7.hellopet.domain.user.service.UserFinder;
import com.sesac7.hellopet.domain.user.service.UserService;
import jakarta.validation.Valid;
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

    private final UserService userService;
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

        ResponseCookie accessCookie = jwtUtil.generateCookie(userDetails);
        ResponseCookie refreshCookie = jwtUtil.generateRefreshCookie(userDetails);
        LoginResponse loginResponse = userService.userLogin(userDetails.getUsername());

        return new AuthResult(accessCookie, refreshCookie, loginResponse);
    }

    public void checkPassword(@Valid CheckPasswordRequest request, CustomUserDetails userDetails) {
        User loggedInUser = userFinder.findLoggedInUserByUsername(userDetails.getUsername());
        if (!userService.verifyLoggedInUserPassword(loggedInUser, request)) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }
    }
}
