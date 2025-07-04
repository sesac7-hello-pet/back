package com.sesac7.hellopet.domain.application.service;

import com.sesac7.hellopet.common.utils.CustomUserDetails;
import com.sesac7.hellopet.domain.announcement.entity.Announcement;
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
import com.sesac7.hellopet.domain.application.validation.DuplicateApplicationException;
import com.sesac7.hellopet.domain.user.entity.User;
import com.sesac7.hellopet.domain.user.service.UserFinder;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
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
                                                               "해당 번호의 입양 신청서를 찾을 수 없습니다. id=" + id)
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
                                                               "해당 번호의 입양 신청서를 찾을 수 없습니다. id=" + id));

        return ApplicationDetailResponse.from(application);
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

    public ApplicationApprovalResponse processApplicationApproval(Long announcementId, Long applicationId) {
        approveAndRejectOtherApplications(announcementId, applicationId);
        announcementService.completeAnnouncement(announcementId);

        return ApplicationApprovalResponse.of(announcementId, applicationId);
    }

    private void approveAndRejectOtherApplications(Long announcementId, Long applicationId) {
        Application application = applicationRepository.findByIdAndAnnouncementIdAndStatus(applicationId,
                                                               announcementId, ApplicationStatus.PENDING)
                                                       .orElseThrow(() -> new AlreadyProcessedApplicationException());

        application.changeStatus(ApplicationStatus.APPROVED);

        List<Application> otherApplications = applicationRepository.findByAnnouncementIdAndExcludeApplicationId(
                announcementId, applicationId);

        for (Application otherApplication : otherApplications) {
            otherApplication.changeStatus(ApplicationStatus.REJECTED);
        }
    }
}
