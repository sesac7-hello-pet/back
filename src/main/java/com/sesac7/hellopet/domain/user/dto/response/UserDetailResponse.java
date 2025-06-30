package com.sesac7.hellopet.domain.user.dto.response;

import com.sesac7.hellopet.domain.user.entity.UserDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDetailResponse {
    private String nickname;
    private String username;
    private String address;
    private String phoneNumber;

    public static UserDetailResponse from(UserDetail userDetail) {
        return new UserDetailResponse(userDetail.getNickname(), userDetail.getUsername(), userDetail.getAddress(),
                userDetail.getPhoneNumber());
    }
}
