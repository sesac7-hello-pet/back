package com.sesac7.hellopet.domain.application.dto.request;

import lombok.Getter;

@Getter
public class ApplicationCreateRequest {
    private Long announcementId;
    private String reason;
    private HousingInfoRequest housingInfo;
    private FamilyInfoRequest familyInfo;
    private CareInfoRequest careInfo;
    private FinancialInfoRequest financialInfo;
    private PetExperienceInfoRequest petExperienceInfo;
    private FuturePlanInfoRequest futurePlanInfo;
    private boolean agreedToTerms;
}
