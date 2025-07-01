package com.sesac7.hellopet.domain.user.dto.response;

import com.sesac7.hellopet.domain.user.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminUserListResponse {
    private Long id;
    private String email;
    private UserRole role;
    private String nickname;
    private String username;
    private String phoneNumber;
}
