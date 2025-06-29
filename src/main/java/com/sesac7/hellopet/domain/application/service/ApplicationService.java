package com.sesac7.hellopet.domain.application.service;

import com.sesac7.hellopet.domain.application.dto.request.ApplicationCreateRequest;
import com.sesac7.hellopet.domain.application.dto.request.CareInfoRequest;
import com.sesac7.hellopet.domain.application.dto.request.FamilyInfoRequest;
import com.sesac7.hellopet.domain.application.dto.request.FinancialInfoRequest;
import com.sesac7.hellopet.domain.application.dto.request.FuturePlanInfoRequest;
import com.sesac7.hellopet.domain.application.dto.request.HousingInfoRequest;
import com.sesac7.hellopet.domain.application.dto.request.PetExperienceInfoRequest;
import com.sesac7.hellopet.domain.application.dto.response.ApplicationResponse;
import com.sesac7.hellopet.domain.application.entity.Application;
import com.sesac7.hellopet.domain.application.entity.info.care.CareInfo;
import com.sesac7.hellopet.domain.application.entity.info.experience.PetExperienceInfo;
import com.sesac7.hellopet.domain.application.entity.info.family.FamilyInfo;
import com.sesac7.hellopet.domain.application.entity.info.financial.FinancialInfo;
import com.sesac7.hellopet.domain.application.entity.info.housing.HousingInfo;
import com.sesac7.hellopet.domain.application.entity.info.plan.FuturePlanInfo;
import com.sesac7.hellopet.domain.application.repository.ApplicationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    // private final UserService userService;
    // private final AnnouncementService announcementService;

    public ApplicationResponse createApplication(ApplicationCreateRequest request) {
        // 현재 로그인 사용자(경우님 담당)
        // User user = 관련 메서드

        // 공고 가져오기(우정님 담당)
        // Announcement announcement = announcementService.findById(request.getAnnouncementId());

        Application application = Application.builder()
                                             // .user(user)
                                             // .announcement(announcement)
                                             .reason(request.getReason())
                                             .housingInfo(getHousingInfo(request))
                                             .familyInfo(getFamilyInfo(request))
                                             .careInfo(getCareInfo(request))
                                             .financialInfo(getFinancialInfo(request))
                                             .petExperienceInfo(getPetExperienceInfo(request))
                                             .futurePlanInfo(getFuturePlanInfo(request))
                                             .agreedToTerms(request.isAgreedToTerms())
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
}
