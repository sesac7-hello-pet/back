package com.sesac7.hellopet.domain.application.entity.environment;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FamilyInfo {

    private int householdMembers;
    private boolean familyAgreement;
    private boolean hasChildrenUnder13;

    @Builder
    public FamilyInfo(int householdMembers, boolean familyAgreement, boolean hasChildrenUnder13) {
        this.householdMembers = householdMembers;
        this.familyAgreement = familyAgreement;
        this.hasChildrenUnder13 = hasChildrenUnder13;
    }
}
