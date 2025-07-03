package com.sesac7.hellopet.global.exception.custom;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() {
        super("Refresh 토큰이 없습니다.");
    }
}
