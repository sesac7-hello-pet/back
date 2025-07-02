package com.sesac7.hellopet.domain.user.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum UserSortType {
    ID, ROLE, USERNAME, EMAIL, NICKNAME, PHONENUMBER;

    /**
     * 문자열을 UserSortType으로 변환합니다.
     * null 이거나 매칭되지 않으면 ID를 기본 반환.
     */
    @JsonCreator
    public static UserSortType toEnum(String str) {
        if (str == null) {
            return ID;
        }
        try {
            return UserSortType.valueOf(str.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ID;
        }
    }
}
