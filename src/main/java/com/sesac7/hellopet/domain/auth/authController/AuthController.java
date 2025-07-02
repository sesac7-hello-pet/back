package com.sesac7.hellopet.domain.auth.authController;

import com.sesac7.hellopet.common.utils.CustomUserDetails;
import com.sesac7.hellopet.domain.auth.authService.AuthService;
import com.sesac7.hellopet.domain.auth.dto.request.CheckPasswordRequest;
import com.sesac7.hellopet.domain.auth.dto.request.LoginRequest;
import com.sesac7.hellopet.domain.auth.dto.response.AuthResult;
import com.sesac7.hellopet.domain.auth.dto.response.CheckPasswordResponse;
import com.sesac7.hellopet.domain.auth.dto.response.LoginResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResult result = authService.userLogin(request);

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, result.getAccessCookie().toString())
                .header(HttpHeaders.SET_COOKIE, result.getRefreshCookie().toString())
                .body(result.getLoginResponse());
    }

    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout() {
        AuthResult result = authService.userLogout();
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .header(HttpHeaders.SET_COOKIE, result.getAccessCookie().toString())
                .header(HttpHeaders.SET_COOKIE, result.getRefreshCookie().toString())
                .build();
    }

    @PostMapping("/check-password")
    public ResponseEntity<CheckPasswordResponse> check(@Valid @RequestBody CheckPasswordRequest request,
                                                       @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.checkPassword(request, userDetails));
    }
}
