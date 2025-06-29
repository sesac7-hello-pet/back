package com.sesac7.hellopet.domain.application.entity.info.family;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FamilyAgreement {

    ALL_AGREE("모두 동의"),
    SOME_DISAGREE("일부 반대");

    private final String label;
}
