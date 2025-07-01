package com.sesac7.hellopet.domain.application.service;

import com.sesac7.hellopet.common.utils.CustomUserDetails;
import com.sesac7.hellopet.domain.announcement.entity.Announcement;
import com.sesac7.hellopet.domain.announcement.service.AnnouncementService;
import com.sesac7.hellopet.domain.application.dto.request.AgreementInfoRequest;
import com.sesac7.hellopet.domain.application.dto.request.ApplicationCreateRequest;
import com.sesac7.hellopet.domain.application.dto.request.CareInfoRequest;
import com.sesac7.hellopet.domain.application.dto.request.FamilyInfoRequest;
import com.sesac7.hellopet.domain.application.dto.request.FinancialInfoRequest;
import com.sesac7.hellopet.domain.application.dto.request.FuturePlanInfoRequest;
import com.sesac7.hellopet.domain.application.dto.request.HousingInfoRequest;
import com.sesac7.hellopet.domain.application.dto.request.PetExperienceInfoRequest;
import com.sesac7.hellopet.domain.application.dto.response.ApplicationResponse;
import com.sesac7.hellopet.domain.application.entity.Application;
import com.sesac7.hellopet.domain.application.entity.info.agreement.AgreementInfo;
import com.sesac7.hellopet.domain.application.entity.info.care.CareInfo;
import com.sesac7.hellopet.domain.application.entity.info.experience.PetExperienceInfo;
import com.sesac7.hellopet.domain.application.entity.info.family.FamilyInfo;
import com.sesac7.hellopet.domain.application.entity.info.financial.FinancialInfo;
import com.sesac7.hellopet.domain.application.entity.info.housing.HousingInfo;
import com.sesac7.hellopet.domain.application.entity.info.plan.FuturePlanInfo;
import com.sesac7.hellopet.domain.application.repository.ApplicationRepository;
import com.sesac7.hellopet.domain.user.entity.User;
import com.sesac7.hellopet.domain.user.service.UserFinder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserFinder userFinder;
    private final AnnouncementService announcementService;

    public ApplicationResponse createApplication(ApplicationCreateRequest request, CustomUserDetails userDetails) {

        User user = userFinder.findLoggedInUserByUsername(userDetails.getUsername());
        Announcement announcement = announcementService.findById(request.getAnnouncementId());

        Application application = Application.builder()
                .user(user)
                .announcement(announcement)
                .reason(request.getReason())
                .housingInfo(getHousingInfo(request))
                .familyInfo(getFamilyInfo(request))
                .careInfo(getCareInfo(request))
                .financialInfo(getFinancialInfo(request))
                .petExperienceInfo(getPetExperienceInfo(request))
                .futurePlanInfo(getFuturePlanInfo(request))
                .agreementInfo(getAgreementInfo(request))
                .build();

        applicationRepository.save(application);

        return ApplicationResponse.builder()
                .applicationId(application.getId())
                .message("신청이 완료되었습니다.")
                .build();

    }

    private HousingInfo getHousingInfo(ApplicationCreateRequest request) {
        HousingInfoRequest req = request.getHousingInfo();
        return new HousingInfo(
                req.getHousingType(),
                req.getResidenceType(),
                req.isPetAllowed(),
                req.getPetLivingPlace(),
                req.getHouseSizeRange()
        );
    }

    private FamilyInfo getFamilyInfo(ApplicationCreateRequest request) {
        FamilyInfoRequest req = request.getFamilyInfo();
        return new FamilyInfo(
                req.getNumberOfHousehold(),
                req.isHasChildUnder13(),
                req.getFamilyAgreement(),
                req.isHasPetAllergy()
        );
    }

    private CareInfo getCareInfo(ApplicationCreateRequest request) {
        CareInfoRequest req = request.getCareInfo();
        return new CareInfo(
                req.getAbsenceTime(),
                req.getCareTime()
        );
    }

    private FinancialInfo getFinancialInfo(ApplicationCreateRequest request) {
        FinancialInfoRequest req = request.getFinancialInfo();
        return new FinancialInfo(
                req.getMonthlyBudget(),
                req.isHasEmergencyFund()
        );
    }

    private PetExperienceInfo getPetExperienceInfo(ApplicationCreateRequest request) {
        PetExperienceInfoRequest req = request.getPetExperienceInfo();
        return new PetExperienceInfo(
                req.isHasPetExperience(),
                req.getExperienceDetails()
        );
    }

    private FuturePlanInfo getFuturePlanInfo(ApplicationCreateRequest request) {
        FuturePlanInfoRequest req = request.getFuturePlanInfo();
        return new FuturePlanInfo(
                req.isHasFuturePlan(),
                req.getPlanDetails()
        );
    }

    private AgreementInfo getAgreementInfo(ApplicationCreateRequest request) {
        AgreementInfoRequest req = request.getAgreement();
        return new AgreementInfo(
                req.getAgreedToAccuracy(),
                req.getAgreedToCare(),
                req.getAgreedToPrivacy()
        );
    }
}
