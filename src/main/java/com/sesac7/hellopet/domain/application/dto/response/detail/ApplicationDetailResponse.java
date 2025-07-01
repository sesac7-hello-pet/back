package com.sesac7.hellopet.domain.application.dto.response.detail;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApplicationDetailResponse {
    private Long announcementId;
    private String name;
    private String phoneNumber;
    private String email;
    private String reason;
    private HousingInfoResponse housing;
    private FamilyInfoResponse family;
    private CareInfoResponse care;
    private FinancialInfoResponse financial;
    private PetExperienceInfoResponse petExperience;
    private FuturePlanInfoResponse futurePlan;
    private AgreementInfoResponse agreement;
    private LocalDateTime submittedAt;
}

