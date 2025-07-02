package com.sesac7.hellopet.domain.application.dto.response.detail;

import com.sesac7.hellopet.domain.application.entity.Application;
import com.sesac7.hellopet.domain.user.entity.User;
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

    public static ApplicationDetailResponse from(Application application) {
        User user = application.getApplicant();

        return ApplicationDetailResponse.builder()
                                        .announcementId(application.getAnnouncement().getId())
                                        .name(user.getUserDetail().getUsername())
                                        .phoneNumber(user.getUserDetail().getPhoneNumber())
                                        .email(user.getEmail())
                                        .reason(application.getReason())
                                        .housing(HousingInfoResponse.from(application.getHousingInfo()))
                                        .family(FamilyInfoResponse.from(application.getFamilyInfo()))
                                        .care(CareInfoResponse.from(application.getCareInfo()))
                                        .financial(FinancialInfoResponse.from(application.getFinancialInfo()))
                                        .petExperience(PetExperienceInfoResponse.from(
                                                application.getPetExperienceInfo())
                                        )
                                        .futurePlan(FuturePlanInfoResponse.from(application.getFuturePlanInfo()))
                                        .agreement(AgreementInfoResponse.from(application.getAgreementInfo()))
                                        .submittedAt(application.getSubmittedAt())
                                        .build();
    }
}
