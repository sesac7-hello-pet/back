package com.sesac7.hellopet.domain.application.dto.response.detail;

import com.sesac7.hellopet.domain.application.entity.info.family.FamilyInfo;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FamilyInfoResponse {
    private int numberOfHousehold;
    private boolean hasChildUnder13;
    private String familyAgreement;
    private boolean hasPetAllergy;

    public static FamilyInfoResponse from(FamilyInfo familyInfo) {
        return FamilyInfoResponse.builder()
                                 .numberOfHousehold(familyInfo.getNumberOfHousehold())
                                 .hasChildUnder13(familyInfo.isHasChildUnder13())
                                 .familyAgreement(familyInfo.getFamilyAgreement().name())
                                 .hasPetAllergy(familyInfo.isHasPetAllergy())
                                 .build();
    }
}
