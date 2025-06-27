package com.sesac7.hellopet.domain.application.entity.environment;

import lombok.Getter;

@Getter
public enum HousingType {
    APARTMENT("아파트"),
    VILLA("빌라 / 연립주택"),
    DETACHED_HOUSE("단독주택"),
    DORMITORY("고시원 / 기숙사 / 쉐어하우스"),
    OTHER("기타");

    private final String label;

    HousingType(String label) {
        this.label = label;
    }
}
