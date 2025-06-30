package com.sesac7.hellopet.domain.application.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ApplicationCreateRequest {
    @NotNull
    private Long announcementId;

    @NotBlank(message = "신청 희망 사유는 필수 입력 항목입니다.")
    private String reason;

    @Valid
    private HousingInfoRequest housingInfo;

    @Valid
    private FamilyInfoRequest familyInfo;

    @Valid
    private CareInfoRequest careInfo;

    @Valid
    private FinancialInfoRequest financialInfo;

    @Valid
    private PetExperienceInfoRequest petExperienceInfo;

    @Valid
    private FuturePlanInfoRequest futurePlanInfo;

    @Valid
    private AgreementInfoRequest agreement;
}
