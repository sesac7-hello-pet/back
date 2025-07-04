package com.sesac7.hellopet.domain.application.dto.request;

import com.sesac7.hellopet.domain.application.entity.info.housing.HouseSizeRange;
import com.sesac7.hellopet.domain.application.entity.info.housing.HousingType;
import com.sesac7.hellopet.domain.application.entity.info.housing.PetLivingPlace;
import com.sesac7.hellopet.domain.application.entity.info.housing.ResidenceType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class HousingInfoRequest {

    @NotNull(message = "주거 형태는 필수 입력 항목입니다.")
    private HousingType housingType;

    @NotNull(message = "거주 형태는 필수 입력 항목입니다.")
    private ResidenceType residenceType;

    @NotNull(message = "반려동물 허용 여부는 필수 입력 항목입니다.")
    private Boolean petAllowed;

    @NotNull(message = "반려동물 생활 공간은 필수 입력 항목입니다.")
    private PetLivingPlace petLivingPlace;

    @NotNull(message = "주거 면적은 필수 입력 항목입니다.")
    private HouseSizeRange houseSizeRange;
}
