package com.sesac7.hellopet.domain.announcement.service;

import com.sesac7.hellopet.domain.announcement.dto.request.PetCreateRequestDTO;
import com.sesac7.hellopet.domain.announcement.dto.response.AnnoucementListDTO;
import com.sesac7.hellopet.domain.announcement.entity.Announcement;
import com.sesac7.hellopet.domain.announcement.entity.Pet;
import com.sesac7.hellopet.domain.announcement.repository.AnnouncementRepository;
import com.sesac7.hellopet.domain.announcement.repository.PetRepository;
import com.sesac7.hellopet.domain.announcement.repository.ShelterRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

//package com.sesac7.hellopet.domain.announcement.dto.request;
//

/// / 동물 등록 시 클라이언트가 보내는 요청값(post 요청)
//public class PetCreateRequestDTO {
//    private String annoucementId; // 공고번호 (문자열)
//    private String breed;              // 견종
//    private String gender;             // 성별
//    private String health;             // 건강상태
//    private String personality;        // 성격
//    private int age;                   // 나이
//    private Long shelterId;            // 보호소 ID
//}
@Service
@RequiredArgsConstructor
@Transactional
public class AnnouncementSerivce {
    private final PetRepository petRepository;
    private final AnnouncementRepository announcementRepository;
    private final ShelterRepository shelterRepository;

    // 게시글 등록
    // 게시글 등록
    public Long addAnnouncement(PetCreateRequestDTO petCreateRequestDTO) {
        // 1. shelter는 DTO에 id로 들어오므로, 엔티티 조회가 필요함
        Shelter shelter = shelterRepository.findById(petCreateRequestDTO.getShelterId())
                .orElseThrow(() -> new IllegalArgumentException("보호소를 찾을 수 없습니다."));

        // 2. Pet 엔티티 생성
        Pet pet = Pet.builder()
                .announcementId(petCreateRequestDTO.getPublicNoticeNumber())
                .breed(petCreateRequestDTO.getBreed())
                .gender(petCreateRequestDTO.getGender())
                .health(petCreateRequestDTO.getHealth())
                .personality(petCreateRequestDTO.getPersonality())
                .age(petCreateRequestDTO.getAge())
                .shelter(shelter)
                .build();

        petRepository.save(pet); // 먼저 저장

        // 3. Announcement(Post) 생성
        Announcement announcement = Announcement.builder()
                .shelter(shelter)
                .pet(pet)
                .status("입양중") // 기본값
                .image(petCreateRequestDTO.getImage())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Announcement saved = announcementRepository.save(announcement);

        return ; // 등록된 게시글 ID 반환
    }


}
