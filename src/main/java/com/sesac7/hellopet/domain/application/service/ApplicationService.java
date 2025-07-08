package com.sesac7.hellopet.domain.application.service;

import com.sesac7.hellopet.common.utils.CustomUserDetails;
import com.sesac7.hellopet.domain.announcement.entity.Announcement;
import com.sesac7.hellopet.domain.announcement.entity.AnnouncementStatus;
import com.sesac7.hellopet.domain.announcement.service.AnnouncementService;
import com.sesac7.hellopet.domain.application.dto.request.ApplicationCreateRequest;
import com.sesac7.hellopet.domain.application.dto.request.ApplicationPageRequest;
import com.sesac7.hellopet.domain.application.dto.response.ApplicationApprovalResponse;
import com.sesac7.hellopet.domain.application.dto.response.ApplicationResponse;
import com.sesac7.hellopet.domain.application.dto.response.ShelterApplicationResponse;
import com.sesac7.hellopet.domain.application.dto.response.ShelterApplicationsPageResponse;
import com.sesac7.hellopet.domain.application.dto.response.UserApplicationPageResponse;
import com.sesac7.hellopet.domain.application.dto.response.UserApplicationResponse;
import com.sesac7.hellopet.domain.application.dto.response.detail.ApplicationDetailResponse;
import com.sesac7.hellopet.domain.application.entity.Application;
import com.sesac7.hellopet.domain.application.entity.ApplicationStatus;
import com.sesac7.hellopet.domain.application.repository.ApplicationRepository;
import com.sesac7.hellopet.domain.application.validation.AlreadyProcessedApplicationException;
import com.sesac7.hellopet.domain.application.validation.AnnouncementAlreadyCompletedException;
import com.sesac7.hellopet.domain.application.validation.AnnouncementApprovalPermissionException;
import com.sesac7.hellopet.domain.application.validation.ApplicationAlreadyApprovedException;
import com.sesac7.hellopet.domain.application.validation.DuplicateApplicationException;
import com.sesac7.hellopet.domain.user.entity.User;
import com.sesac7.hellopet.domain.user.entity.UserRole;
import com.sesac7.hellopet.domain.user.service.UserFinder;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
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
                                                               "해당 번호의 입양 신청서를 찾을 수 없습니다. id=" + id)
                                                       );

        User user = userFinder.findLoggedInUserByUsername(userDetails.getUsername());

        if (!application.getApplicant().getId().equals(user.getId())) {
            throw new AccessDeniedException("입양 신청서를 삭제할 권한이 없습니다.");
        }

        if (application.getStatus() == ApplicationStatus.APPROVED) {
            throw new ApplicationAlreadyApprovedException(application.getId());
        }

        applicationRepository.delete(application);
    }

    @Transactional(readOnly = true)
    public ApplicationDetailResponse getApplication(Long id) {
        Application application = applicationRepository.findById(id)
                                                       .orElseThrow(() -> new EntityNotFoundException(
                                                               "해당 번호의 입양 신청서를 찾을 수 없습니다. id=" + id));

        return ApplicationDetailResponse.from(application);
    }

    @Transactional(readOnly = true)
    public ShelterApplicationsPageResponse getShelterApplications(Long id,
                                                                  ApplicationPageRequest request,
                                                                  CustomUserDetails userDetails) {

        Announcement announcement = announcementService.findById(id);
        User shelter = userFinder.findLoggedInUserByUsername(userDetails.getUsername());

        if (!announcement.getShelter().getId().equals(shelter.getId())) {
            throw new AccessDeniedException("해당 공고에 대한 접근 권한이 없습니다.");
        }

        Pageable pageable = request.toPageable();
        Page<Application> page = applicationRepository.findByAnnouncementId(id, pageable);

        List<ShelterApplicationResponse> content = page.stream()
                                                       .map(app -> ShelterApplicationResponse.from(app))
                                                       .toList();

        return ShelterApplicationsPageResponse.of(pageable, content, page.getTotalElements(), announcement);
    }

    @Transactional(readOnly = true)
    public UserApplicationPageResponse getUserApplications(CustomUserDetails userDetails,
                                                           ApplicationPageRequest request) {
        User user = userFinder.findLoggedInUserByUsername(userDetails.getUsername());
        Pageable pageable = request.toPageable();

        Page<Application> page = applicationRepository.findByApplicantId(user.getId(), pageable);

        List<UserApplicationResponse> content = page.stream()
                                                    .map(app -> UserApplicationResponse.of(app, app.getAnnouncement()))
                                                    .toList();

        return UserApplicationPageResponse.of(pageable, content, page.getTotalElements());
    }

    public ApplicationResponse createApplication(ApplicationCreateRequest request, CustomUserDetails userDetails) {

        User user = userFinder.findLoggedInUserByUsername(userDetails.getUsername());
        Announcement announcement = announcementService.findById(request.getAnnouncementId());

        if (announcement.getStatus() == AnnouncementStatus.COMPLETED) {
            throw new AnnouncementAlreadyCompletedException(announcement.getId());
        }

        Optional<Application> existingApplication = applicationRepository.findByApplicantIdAndAnnouncementId(
                user.getId(),
                announcement.getId()
        );

        if (existingApplication.isPresent()) {
            throw new DuplicateApplicationException();
        }

        Application application = request.toEntity(user, announcement);
        applicationRepository.save(application);

        return ApplicationResponse.from(application.getId());
    }

    public ApplicationApprovalResponse processApplicationApproval(Long announcementId,
                                                                  Long applicationId,
                                                                  UserDetails userDetails) {
        // 보호소의 공고 승인 권한 검증
        validateShelterApprovalPermission(announcementId, userDetails);

        // 해당 공고에 대해 신청서 승인 및 나머지 신청서 일괄 거절 처리
        approveAndRejectApplications(announcementId, applicationId);

        // 공고 상태를 완료로 변경
        announcementService.completeAnnouncement(announcementId);

        return ApplicationApprovalResponse.of(announcementId, applicationId);
    }

    private void validateShelterApprovalPermission(Long announcementId, UserDetails userDetails) {
        User loginUser = userFinder.findLoggedInUserByUsername(userDetails.getUsername());
        Announcement announcement = announcementService.findById(announcementId);

        boolean isShelter = loginUser.getRole() == UserRole.SHELTER;
        boolean isShelterOwner = announcement.getShelter().getId().equals(loginUser.getId());

        if (!(isShelter && isShelterOwner)) {
            throw new AnnouncementApprovalPermissionException();
        }
    }

    private void approveAndRejectApplications(Long announcementId, Long applicationId) {
        Application approvedApp = applicationRepository.findByIdAndAnnouncementIdAndStatus(
                                                               applicationId, announcementId, ApplicationStatus.PENDING
                                                       )
                                                       .orElseThrow(() -> new AlreadyProcessedApplicationException());

        approvedApp.approve();

        List<Application> rejectedApps = applicationRepository.findByAnnouncementIdAndExcludeApplicationId(
                announcementId, applicationId);

        rejectedApps.forEach(other -> other.reject());
    }
}
