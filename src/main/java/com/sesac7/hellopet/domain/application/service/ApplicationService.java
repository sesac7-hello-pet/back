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
import com.sesac7.hellopet.domain.application.dto.response.UserApplicationPageResponse;
import com.sesac7.hellopet.domain.application.dto.response.UserApplicationResponse;
import com.sesac7.hellopet.domain.application.dto.response.detail.AgreementInfoResponse;
import com.sesac7.hellopet.domain.application.dto.response.detail.ApplicationDetailResponse;
import com.sesac7.hellopet.domain.application.dto.response.detail.CareInfoResponse;
import com.sesac7.hellopet.domain.application.dto.response.detail.FamilyInfoResponse;
import com.sesac7.hellopet.domain.application.dto.response.detail.FinancialInfoResponse;
import com.sesac7.hellopet.domain.application.dto.response.detail.FuturePlanInfoResponse;
import com.sesac7.hellopet.domain.application.dto.response.detail.HousingInfoResponse;
import com.sesac7.hellopet.domain.application.dto.response.detail.PetExperienceInfoResponse;
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
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserFinder userFinder;
    private final AnnouncementService announcementService;

    public void deleteApplication(Long id, CustomUserDetails userDetails) {
        Application application = applicationRepository.findById(id)
                                                       .orElseThrow(() -> new EntityNotFoundException(
                                                               "해당 입양 신청서를 찾을 수 없습니다. id=" + id)
                                                       );

        User user = userFinder.findLoggedInUserByUsername(userDetails.getUsername());

        if (!application.getApplicant().getId().equals(user.getId())) {
            throw new AccessDeniedException("입양 신청서를 삭제할 권한이 없습니다.");
        }

        applicationRepository.delete(application);
    }

    @Transactional(readOnly = true)
    public ApplicationDetailResponse getApplication(Long id) {
        Application application = applicationRepository.findById(id)
                                                       .orElseThrow(() -> new EntityNotFoundException(
                                                               "해당 번호의 신청서를 찾을 수 없습니다. id=" + id));

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
                .map(application -> ShelterApplicationResponse.of(announcement, application))
                .toList();

        return ShelterApplicationsPageResponse.of(pageable, content, applications.size());
    }

    @Transactional(readOnly = true)
    public UserApplicationPageResponse getUserApplications(CustomUserDetails userDetails,
                                                           ApplicationPageRequest request) {
        User user = userFinder.findLoggedInUserByUsername(userDetails.getUsername());

        List<Application> applications = applicationRepository.findApplicationsWithAnnouncementByApplicantId(
                user.getId());

        Pageable pageable = request.toPageable();
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), applications.size());

        List<UserApplicationResponse> content = applications
                .subList(start, end)
                .stream()
                .map(application -> UserApplicationResponse.of(application, application.getAnnouncement()))
                .toList();

        return UserApplicationPageResponse.of(pageable, content, applications.size());
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
