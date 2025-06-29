package com.sesac7.hellopet.domain.application.dto.request;

import com.sesac7.hellopet.domain.application.entity.info.housing.HouseSizeRange;
import com.sesac7.hellopet.domain.application.entity.info.housing.HousingType;
import com.sesac7.hellopet.domain.application.entity.info.housing.PetLivingPlace;
import com.sesac7.hellopet.domain.application.entity.info.housing.ResidenceType;
import lombok.Getter;

@Getter
public class HousingInfoRequest {
    private HousingType housingType;
    private ResidenceType residenceType;
    private boolean petAllowed;
    private PetLivingPlace petLivingPlace;
    private HouseSizeRange houseSizeRange;
}
