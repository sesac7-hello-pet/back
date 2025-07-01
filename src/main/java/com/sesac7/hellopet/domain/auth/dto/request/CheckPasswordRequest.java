package com.sesac7.hellopet.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CheckPasswordRequest {
    @NotBlank(message = "비밀번호는 필수입니다")
    private String password;
}
