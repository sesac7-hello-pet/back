package com.sesac7.hellopet.domain.application.entity;

import lombok.Getter;

@Getter
public enum ApplicationType {
    ADOPTION("입양");
    // TEMPORARY_PROTECTION("위탁");

    private final String label;

    ApplicationType(String label) {
        this.label = label;
    }
}
