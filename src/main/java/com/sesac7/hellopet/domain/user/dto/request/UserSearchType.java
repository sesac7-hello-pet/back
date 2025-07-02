package com.sesac7.hellopet.domain.user.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum UserSearchType {
    TOTAL, USERNAME, EMAIL, NICKNAME;

    /**
     * 문자열을 UserSearchType으로 변환합니다.
     * null 이거나 매칭되지 않으면 TOTAL을 기본 반환.
     */
    @JsonCreator
    public static UserSearchType toEnum(String str) {
        if (str == null) {
            return TOTAL;
        }
        try {
            return UserSearchType.valueOf(str.toUpperCase());
        } catch (IllegalArgumentException e) {
            return TOTAL;
        }
    }
}
