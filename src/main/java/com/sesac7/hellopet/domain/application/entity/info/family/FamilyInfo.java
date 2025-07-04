package com.sesac7.hellopet.domain.application.entity.info.family;

import com.sesac7.hellopet.domain.application.dto.request.FamilyInfoRequest;
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
public class FamilyInfo {

    @Column(nullable = false)
    private int numberOfHousehold;

    @Column(nullable = false)
    private boolean hasChildUnder13;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FamilyAgreement familyAgreement;

    @Column(nullable = false)
    private boolean hasPetAllergy;

    public static FamilyInfo from(FamilyInfoRequest request) {
        return new FamilyInfo(
                request.getNumberOfHousehold(),
                request.getHasChildUnder13(),
                request.getFamilyAgreement(),
                request.getHasPetAllergy()
        );
    }
}

