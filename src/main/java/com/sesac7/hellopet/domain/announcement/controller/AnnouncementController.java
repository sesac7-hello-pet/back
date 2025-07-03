package com.sesac7.hellopet.domain.announcement.controller;

import com.sesac7.hellopet.common.utils.CustomUserDetails;
import com.sesac7.hellopet.domain.announcement.dto.request.AnnouncementCreateRequest;
import com.sesac7.hellopet.domain.announcement.dto.request.AnnouncementSearchRequest;
import com.sesac7.hellopet.domain.announcement.dto.request.AnnouncementUpdateRequest;
import com.sesac7.hellopet.domain.announcement.dto.response.AnnouncementCreateResponse;
import com.sesac7.hellopet.domain.announcement.dto.response.AnnouncementDetailResponse;
import com.sesac7.hellopet.domain.announcement.dto.response.AnnouncementListResponse;
import com.sesac7.hellopet.domain.announcement.dto.response.AnnouncementPageResponse;
import com.sesac7.hellopet.domain.announcement.service.AnnouncementService;
import com.sesac7.hellopet.domain.application.dto.request.ApplicationPageRequest;
import com.sesac7.hellopet.domain.application.dto.response.ApplicationApprovalResponse;
import com.sesac7.hellopet.domain.application.dto.response.ShelterApplicationsPageResponse;
import com.sesac7.hellopet.domain.application.service.ApplicationService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/announcements")
@RequiredArgsConstructor
public class AnnouncementController {

    private final AnnouncementService announcementService;
    private final ApplicationService applicationService;

    /**
     * 게시글 등록
     *
     * @param announcementCreateRequest
     * @param customUserDetails
     * @return
     */
    @PostMapping
    public ResponseEntity<AnnouncementCreateResponse> createAnnouncement(
            @RequestBody AnnouncementCreateRequest announcementCreateRequest,
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.status(HttpStatus.CREATED).body(announcementService.createAnnouncement(
                announcementCreateRequest, customUserDetails));

    }

    /**
     * 전체 입양 게시글 리스트 조회
     */

    @GetMapping
    public ResponseEntity <AnnouncementPageResponse> getAllAnnouncements(@ModelAttribute AnnouncementSearchRequest request) {
        return ResponseEntity.ok(announcementService.getAllAnnouncements(request));
    }


    /**
     * 공고별 신청 내역 조회
     *
     * @param id      공고 ID
     * @param request 페이지 정보를 담은 요청 DTO
     * @return 공고 ID에 해당는 공고에 접수된 신청서 리스트 및 페이지 정보
     */
    @GetMapping("/{id}/applications")
    public ResponseEntity<ShelterApplicationsPageResponse> getApplications(
            @PathVariable Long id,
            @ModelAttribute @Valid ApplicationPageRequest request) {

        ShelterApplicationsPageResponse response = applicationService.getShelterApplications(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * 입양 신청서 상태를 승인 및 거절로 처리하고 공고를 완료 처리
     *
     * @param announcementId 공고 ID
     * @param applicationId  승인할 신청서 ID
     * @return 상태 변경 결과 응답
     */
    @PutMapping("/{announcementId}/applications/{applicationId}")
    public ResponseEntity<ApplicationApprovalResponse> updateApplicationStatus(
            @PathVariable Long announcementId,
            @PathVariable Long applicationId) {

        ApplicationApprovalResponse response = applicationService.processApplicationApproval(
                announcementId,
                applicationId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    /***
     * 입양 리스트 상세 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<AnnouncementDetailResponse> getAnnouncementDetail(@PathVariable Long id) {
        AnnouncementDetailResponse detail = announcementService.getAnnouncementDetail(id);
        return ResponseEntity.ok(detail);
    }

    /***
     * 입양 공고 게시글 수정
     */
    @PutMapping("/{id}")
    public ResponseEntity<AnnouncementUpdateRequest> updateAnnouncement(
            @PathVariable Long id,
            @RequestBody AnnouncementUpdateRequest announcementUpdateRequest,
            @AuthenticationPrincipal CustomUserDetails userDetails) throws Exception {

        AnnouncementUpdateRequest updated = announcementService.updateAnnouncement(
                id, announcementUpdateRequest, userDetails.getUsername());
        return ResponseEntity.ok(updated);
    }

    /***
     * 입양 공고 게시글 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAnnouncement(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        announcementService.deleteAnnouncement(id, userDetails.getUsername());
        return ResponseEntity.ok("삭제가 완료되었습니다.");
    }
}
