package com.sesac7.hellopet.domain.auth.authController;

import com.sesac7.hellopet.domain.auth.authService.AuthService;
import com.sesac7.hellopet.domain.auth.dto.request.LoginRequest;
import com.sesac7.hellopet.domain.auth.dto.response.AuthResult;
import com.sesac7.hellopet.domain.auth.dto.response.LoginResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

        System.out.println(result.getLoginResponse().getEmail());
        System.out.println();

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, result.getAccessCookie().toString())
                .header(HttpHeaders.SET_COOKIE, result.getRefreshCookie().toString())
                .body(result.getLoginResponse());
    }
}
