package com.sesac7.hellopet.domain.user.dto.request;

public enum UserSearchType {
    TOTAL, USERNAME, EMAIL, NICKNAME;

    /**
     * 문자열을 UserSearchType으로 변환합니다.
     * null 이거나 매칭되지 않으면 TOTAL을 기본 반환.
     */
    public static UserSearchType toEnum(String str) {
        try {
            return UserSearchType.valueOf(str.toUpperCase());
        } catch (IllegalArgumentException e) {
            return TOTAL;
        }
    }
}
