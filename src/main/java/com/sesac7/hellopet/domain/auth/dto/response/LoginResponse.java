package com.sesac7.hellopet.domain.auth.dto.response;

import com.sesac7.hellopet.domain.user.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private Long userId;
    private String email;
    private UserRole role;
    private String nickname;
    private String profileUrl;
    private Boolean activation;
}
