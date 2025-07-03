package com.sesac7.hellopet.domain.announcement.controller;

import com.sesac7.hellopet.common.utils.CustomUserDetails;
import com.sesac7.hellopet.domain.announcement.dto.request.AnnouncementCreateRequest;
import com.sesac7.hellopet.domain.announcement.dto.request.AnnouncementUpdateRequest;
import com.sesac7.hellopet.domain.announcement.dto.response.AnnouncementDetailResponse;
import com.sesac7.hellopet.domain.announcement.dto.response.AnnouncementListResponse;
import com.sesac7.hellopet.domain.announcement.dto.response.AnnouncementCreateResponse;
import com.sesac7.hellopet.domain.announcement.service.AnnouncementService;
import jakarta.persistence.Id;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/announcements")
@RequiredArgsConstructor
public class AnnouncementController {

    private final AnnouncementService announcementService;

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
    public ResponseEntity<List<AnnouncementListResponse>> getAllAnnouncements() {
        List<AnnouncementListResponse> announcements = announcementService.getAllAnnouncements();
        return ResponseEntity.ok(announcements);
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
                id, announcementUpdateRequest, userDetails.getUsername()  );
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

    /***
     * 내가 쓴 입양 공고 조회
     */
    @GetMapping("/my")
    public ResponseEntity<?> getMyAnnouncements(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        String username = userDetails.getUsername();  // 이메일 or 사용자 ID 등

        List<AnnouncementListResponse> myAnnouncements = announcementService.getMyAnnouncements(username);

        return ResponseEntity.ok(myAnnouncements);
    }




}

