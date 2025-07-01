package com.sesac7.hellopet.global.exception.custom;

import org.springframework.security.authentication.DisabledException;

public class WithdrawUserException extends DisabledException {
    public WithdrawUserException() {
        super("탈퇴한(비활성) 유저입니다.");
    }
}
