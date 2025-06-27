package com.sesac7.hellopet.domain.user.dto.response;

import com.sesac7.hellopet.domain.user.entity.User;
import com.sesac7.hellopet.domain.user.entity.UserRole;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserRegisterResponse {
    private Long userId;
    private String email;
    private UserRole role;
    private String userNickname;

    public static UserRegisterResponse from(User user) {
        return new UserRegisterResponse(user.getId(), user.getEmail(), user.getRole(),
                user.getUserDetail().getNickname());
    }
}
