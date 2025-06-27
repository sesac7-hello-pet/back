package com.sesac7.hellopet.domain.user.dto.request;

import com.sesac7.hellopet.domain.user.entity.UserRole;

public class UserRegisterRequest {
    private String email;
    private String password;
    private UserRole role;
    private String nickname;
    private String username;
    private String address;
    private Integer userPhoneNumber;
    private String userProfileUrl;
}
