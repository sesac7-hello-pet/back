package com.sesac7.hellopet.domain.application.dto.response.detail;

import com.sesac7.hellopet.domain.application.entity.info.housing.HousingInfo;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HousingInfoResponse {
    private String housingType;
    private String residenceType;
    private boolean petAllowed;
    private String petLivingPlace;
    private String houseSizeRange;

    public static HousingInfoResponse from(HousingInfo housingInfo) {
        return HousingInfoResponse.builder()
                                  .housingType(housingInfo.getHousingType().name())
                                  .residenceType(housingInfo.getResidenceType().name())
                                  .petAllowed(housingInfo.isPetAllowed())
                                  .petLivingPlace(housingInfo.getPetLivingPlace().name())
                                  .houseSizeRange(housingInfo.getHouseSizeRange().name())
                                  .build();
    }
}
