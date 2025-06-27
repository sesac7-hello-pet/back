package com.sesac7.hellopet.domain.application.entity.environment;

import lombok.Getter;

@Getter
public enum ResidenceType {
    OWNED("자가"),
    LEASED("전세 / 월세"),
    TEMPORARY("임시 거주 (친구/친척 집)");

    private final String label;

    ResidenceType(String label) {
        this.label = label;
    }
}

