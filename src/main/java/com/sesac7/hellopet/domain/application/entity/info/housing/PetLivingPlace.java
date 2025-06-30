package com.sesac7.hellopet.domain.application.entity.info.housing;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PetLivingPlace {
    INDOOR("실내"),
    OUTDOOR("실외"),
    BOTH("실내 + 실외");

    private final String label;
}
