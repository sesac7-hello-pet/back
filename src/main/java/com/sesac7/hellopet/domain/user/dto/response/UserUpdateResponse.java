package com.sesac7.hellopet.domain.user.dto.response;

import com.sesac7.hellopet.domain.user.entity.UserDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserUpdateResponse {

    private String nickname;
    private String address;
    private String profileUrl;

    public static UserUpdateResponse from(UserDetail userDetail) {
        return new UserUpdateResponse(userDetail.getNickname(), userDetail.getAddress(),
                userDetail.getUserProfileUrl());
    }
}
