package com.sesac7.hellopet.domain.auth.authController;

import com.sesac7.hellopet.common.utils.CustomUserDetails;
import com.sesac7.hellopet.domain.auth.authService.AuthService;
import com.sesac7.hellopet.domain.auth.dto.request.CheckPasswordRequest;
import com.sesac7.hellopet.domain.auth.dto.request.LoginRequest;
import com.sesac7.hellopet.domain.auth.dto.response.AuthResult;
import com.sesac7.hellopet.domain.auth.dto.response.CheckPasswordResponse;
import com.sesac7.hellopet.domain.auth.dto.response.LoginResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/refresh")
    public ResponseEntity<Void> reissue(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                             .header(HttpHeaders.SET_COOKIE, authService.reissueAccess(request).toString())
                             .build();
    }

    @PostMapping("/check-password")
    public ResponseEntity<CheckPasswordResponse> check(@Valid @RequestBody CheckPasswordRequest request,
                                                       @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.checkPassword(request, userDetails));
    }
}
