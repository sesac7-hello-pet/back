package com.sesac7.hellopet.domain.application.entity.info.housing;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class HousingInfo {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HousingType housingType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResidenceType residenceType;

    @Column(nullable = false)
    private boolean petAllowed;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PetLivingPlace petLivingPlace;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private HouseSizeRange houseSizeRange;
}
