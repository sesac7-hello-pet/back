package com.sesac7.hellopet.domain.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.ResponseCookie;

@Getter
@AllArgsConstructor
public class AuthResult {
    ResponseCookie accessCookie;
    ResponseCookie refreshCookie;
    LoginResponse loginResponse;
}
