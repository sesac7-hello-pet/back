package com.sesac7.hellopet.domain.application.entity.environment;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HousingInfo {

    @Enumerated(EnumType.STRING)
    private HousingType housingType;

    @Enumerated(EnumType.STRING)
    private ResidenceType residenceType;

    @Enumerated(EnumType.STRING)
    private PetAllowedStatus petAllowed;

    @Builder
    public HousingInfo(HousingType housingType, ResidenceType residenceType, PetAllowedStatus petAllowed) {
        this.housingType = housingType;
        this.residenceType = residenceType;
        this.petAllowed = petAllowed;
    }
}
