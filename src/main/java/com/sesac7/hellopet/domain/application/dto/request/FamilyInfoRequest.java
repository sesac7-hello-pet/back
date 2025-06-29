package com.sesac7.hellopet.domain.application.dto.request;

import com.sesac7.hellopet.domain.application.entity.info.family.FamilyAgreement;
import lombok.Getter;

@Getter
public class FamilyInfoRequest {
    private int numberOfHousehold;
    private boolean hasChildUnder13;
    private FamilyAgreement familyAgreement;
    private boolean hasPetAllergy;
}
