package com.sesac7.hellopet.global.exception.custom;

import lombok.Getter;
import org.springframework.http.ResponseCookie;

@Getter
public class UnauthorizedException extends RuntimeException {

    private final ResponseCookie accessCookie;
    private final ResponseCookie refreshCookie;

    public UnauthorizedException(ResponseCookie accessCookie, ResponseCookie refreshCookie) {
        super("Refresh 토큰이 없습니다.");
        this.accessCookie = accessCookie;
        this.refreshCookie = refreshCookie;
    }
}
