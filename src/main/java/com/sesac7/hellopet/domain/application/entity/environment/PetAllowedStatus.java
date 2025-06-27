package com.sesac7.hellopet.domain.application.entity.environment;

import lombok.Getter;

@Getter
public enum PetAllowedStatus {
    ALLOWED("반려동물 허용"),
    NOT_ALLOWED("불허"),
    UNKNOWN("불확실");

    private final String label;

    PetAllowedStatus(String label) {
        this.label = label;
    }
}
