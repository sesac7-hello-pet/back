package com.sesac7.hellopet.domain.user.dto.response;

import com.sesac7.hellopet.domain.user.entity.User;
import com.sesac7.hellopet.domain.user.entity.UserDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDetailResponse {
    private String email;
    private String nickname;
    private String username;
    private String address;
    private String profileUrl;
    private String phoneNumber;

    public static UserDetailResponse from(User user, UserDetail userDetail) {
        return new UserDetailResponse(
                user.getEmail(),
                userDetail.getNickname(), userDetail.getUsername(), userDetail.getAddress(),
                userDetail.getUserProfileUrl(), userDetail.getPhoneNumber());
    }
}
