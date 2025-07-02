package com.sesac7.hellopet.domain.user.dto.request;

public enum UserSortType {
    ID, ROLE, USERNAME, EMAIL, NICKNAME, PHONENUMBER;

    /**
     * 문자열을 UserSortType으로 변환합니다.
     * null 이거나 매칭되지 않으면 ID를 기본 반환.
     */
    public static UserSortType toEnum(String str) {
        try {
            return UserSortType.valueOf(str.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ID;
        }
    }
}
