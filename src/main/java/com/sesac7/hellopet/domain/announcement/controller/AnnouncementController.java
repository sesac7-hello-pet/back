package com.sesac7.hellopet.domain.announcement.controller;

import com.sesac7.hellopet.common.utils.CustomUserDetails;
import com.sesac7.hellopet.domain.announcement.dto.request.AnnouncementCreateRequest;
import com.sesac7.hellopet.domain.announcement.dto.response.AnnouncementDetailResponse;
import com.sesac7.hellopet.domain.announcement.dto.response.AnnouncementListResponse;
import com.sesac7.hellopet.domain.announcement.dto.response.AnnouncementCreateResponse;
import com.sesac7.hellopet.domain.announcement.service.AnnouncementService;
import jakarta.persistence.Id;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/announcements")
@RequiredArgsConstructor
public class AnnouncementController {

    private final AnnouncementService announcementService;

    /**
     * 게시글 등록
     * @param announcementCreateRequest
     * @param customUserDetails
     * @return
     */
    @PostMapping
    public ResponseEntity<AnnouncementCreateResponse> createAnnouncement(@RequestBody AnnouncementCreateRequest announcementCreateRequest, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
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
}
