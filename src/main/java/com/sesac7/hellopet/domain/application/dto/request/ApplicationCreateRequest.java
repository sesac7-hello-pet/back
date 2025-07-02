package com.sesac7.hellopet.domain.application.dto.request;

import com.sesac7.hellopet.domain.announcement.entity.Announcement;
import com.sesac7.hellopet.domain.application.entity.Application;
import com.sesac7.hellopet.domain.application.entity.info.agreement.AgreementInfo;
import com.sesac7.hellopet.domain.application.entity.info.care.CareInfo;
import com.sesac7.hellopet.domain.application.entity.info.experience.PetExperienceInfo;
import com.sesac7.hellopet.domain.application.entity.info.family.FamilyInfo;
import com.sesac7.hellopet.domain.application.entity.info.financial.FinancialInfo;
import com.sesac7.hellopet.domain.application.entity.info.housing.HousingInfo;
import com.sesac7.hellopet.domain.application.entity.info.plan.FuturePlanInfo;
import com.sesac7.hellopet.domain.user.entity.User;
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

    public Application toEntity(User user, Announcement announcement) {
        return Application.builder()
                          .user(user)
                          .announcement(announcement)
                          .reason(reason)
                          .housingInfo(HousingInfo.from(housingInfo))
                          .familyInfo(FamilyInfo.from(familyInfo))
                          .careInfo(CareInfo.from(careInfo))
                          .financialInfo(FinancialInfo.from(financialInfo))
                          .petExperienceInfo(PetExperienceInfo.from(petExperienceInfo))
                          .futurePlanInfo(FuturePlanInfo.from(futurePlanInfo))
                          .agreementInfo(AgreementInfo.from(agreement))
                          .build();
    }
}
