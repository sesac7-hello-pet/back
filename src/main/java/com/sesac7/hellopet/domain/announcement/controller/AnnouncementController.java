package com.sesac7.hellopet.domain.announcement.controller;

import com.sesac7.hellopet.domain.announcement.entity.Announcement;
import com.sesac7.hellopet.domain.announcement.service.AnnouncementSerivce;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 게시판 controller
@RestController
@RequiredArgsConstructor
@RequestMapping("/api") // 필요시 prefix
public class AnnouncementController {
    private final AnnouncementSerivce announcementSerivce;

    @PostMapping("/pet")
    public ResponseEntity<Announcement> createAnnouncement(@RequestBody Announcement announcement) {
        Long annoucementid = announcementSerivce.registerAnnouncement(dto);
        return ResponseEntity.ok("입양 공고 등록이 완료 되었습니다. " + annoucementid);
    }

//    // 입양 게시판 조회
//    @GetMapping("//announcements")
//    public ResponseEntity<Announcement> getAnnouncement() {
//        return ResponseEntity.ok(announcementSerivce.getAllAnnoucements());
//    }
//
//    // 게시판 상세 조회
//    @GetMapping("/announcements")
//    public ResponseEntity<List<Announcement>> getDetail(@PathVariable Long id) {
//        return ResponseEntity.ok(announcementSerivce.getAnnouncementDetail(id));
//    }
//    // 내가 등록한 동물 목록 조회
//    @GetMapping("/pet/{id}")
//    public ResponseEntity<List><MyPetListDTO>> getMine() {
//        return ResponseEntity.ok(announcementSerivce.getMyPets());
//    }
//    @PutMapping("/{pets}/{id}")
//    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Announcement announcement) {
//        announcementSerivce.updatePet(id, dto);
//        return ResponseEntity.ok("수정이 완료 되었습니다.");
//    }
//
//    @DeleteMapping("/pets/{id}")
//    public ResponseEntity<?> delete(@PathVariable Long id) {
//        announcementSerivce.deletePet(id);
//        return ResponseEntity.ok("삭제가 완료 되었습니다.");
//    }
}


