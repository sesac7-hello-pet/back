package com.sesac7.hellopet.domain.application.service;

import com.sesac7.hellopet.common.utils.CustomUserDetails;
import com.sesac7.hellopet.domain.announcement.entity.Announcement;
import com.sesac7.hellopet.domain.announcement.service.AnnouncementService;
import com.sesac7.hellopet.domain.application.dto.request.AgreementInfoRequest;
import com.sesac7.hellopet.domain.application.dto.request.ApplicationCreateRequest;
import com.sesac7.hellopet.domain.application.dto.request.ApplicationPageRequest;
import com.sesac7.hellopet.domain.application.dto.request.CareInfoRequest;
import com.sesac7.hellopet.domain.application.dto.request.FamilyInfoRequest;
import com.sesac7.hellopet.domain.application.dto.request.FinancialInfoRequest;
import com.sesac7.hellopet.domain.application.dto.request.FuturePlanInfoRequest;
import com.sesac7.hellopet.domain.application.dto.request.HousingInfoRequest;
import com.sesac7.hellopet.domain.application.dto.request.PetExperienceInfoRequest;
import com.sesac7.hellopet.domain.application.dto.response.ApplicationResponse;
import com.sesac7.hellopet.domain.application.dto.response.ShelterApplicationResponse;
import com.sesac7.hellopet.domain.application.dto.response.ShelterApplicationsPageResponse;
import com.sesac7.hellopet.domain.application.dto.response.UserApplicationResponse;
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
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserFinder userFinder;
    private final AnnouncementService announcementService;

    @Transactional(readOnly = true)
    public ShelterApplicationsPageResponse getShelterApplications(Long id, ApplicationPageRequest request) {
        Announcement announcement = announcementService.findById(id);

        List<Application> applications = applicationRepository.findApplicationsWithUserDetailByAnnouncementId(id);

        Pageable pageable = request.toPageable();
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), applications.size());

        List<ShelterApplicationResponse> content = applications
                .subList(start, end)
                .stream()
                .map(application -> ShelterApplicationResponse.from(announcement, application))
                .toList();

        return ShelterApplicationsPageResponse.of(pageable, content, applications.size());
    }

    @Transactional(readOnly = true)
    public Page<UserApplicationResponse> getUserApplications(CustomUserDetails userDetails, Pageable pageable) {
        User user = userFinder.findLoggedInUserByUsername(userDetails.getUsername());

        List<Application> applications = applicationRepository.findApplicationsWithAnnouncementByApplicantId(
                user.getId());

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), applications.size());
        List<Application> paged = applications.subList(start, end);

        List<UserApplicationResponse> content = paged
                .stream()
                .map(application -> {
                    Announcement announcement = application.getAnnouncement();

                    return UserApplicationResponse.builder()
                                                  .applicationId(application.getId())
                                                  .announcementId(announcement.getId())
                                                  .applicationStatusLabel(application.getStatus().name())
                                                  .submittedAt(application.getSubmittedAt())
                                                  .petImageUrl(announcement.getImageUrl())
                                                  .build();
                })
                .toList();

        return new PageImpl<>(content, pageable, applications.size());
    }

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
