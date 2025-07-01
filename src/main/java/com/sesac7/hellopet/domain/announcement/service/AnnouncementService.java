package com.sesac7.hellopet.domain.announcement.service;

import com.sesac7.hellopet.common.utils.CustomUserDetails;
import com.sesac7.hellopet.domain.announcement.dto.request.AnnouncementCreateRequest;
import com.sesac7.hellopet.domain.announcement.dto.response.AnnouncementCreateResponse;
import com.sesac7.hellopet.domain.announcement.dto.response.AnnouncementListResponse;
import com.sesac7.hellopet.domain.announcement.entity.Announcement;
import com.sesac7.hellopet.domain.announcement.entity.AnnouncementStatus;
import com.sesac7.hellopet.domain.announcement.entity.Pet;
import com.sesac7.hellopet.domain.announcement.repository.AnnouncementRepository;
import com.sesac7.hellopet.domain.announcement.repository.PetRepository;
import com.sesac7.hellopet.domain.user.entity.User;
import com.sesac7.hellopet.domain.user.service.UserFinder;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AnnouncementService {
    private final PetRepository petRepository;
    private final AnnouncementRepository announcementRepository;
    private final UserFinder userFinder;

    // 게시글 등록
    public AnnouncementCreateResponse createAnnouncement(AnnouncementCreateRequest announcementCreateRequest,
                                                         CustomUserDetails customUserDetails) {
        Pet pet = Pet.builder()
                .breed(announcementCreateRequest.getBreed())
                .gender(announcementCreateRequest.getGender())
                .age(announcementCreateRequest.getAge())
                .health(announcementCreateRequest.getHealth())
                .personality(announcementCreateRequest.getPersonality())
                .foundPlace("발견 장소")
                .build();

        petRepository.save(pet);

        // userRepository는 필드로 선언돼 있고, 스프링이 생성자 주입해줌
        User shelter = userFinder.findLoggedInUserByUsername(customUserDetails.getUsername());

        Announcement announcement = Announcement.builder()
                .shelter(shelter)
                .pet(pet)
                .imageUrl(announcementCreateRequest.getImage())
                .status(AnnouncementStatus.IN_PROGRESS)
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();

        announcementRepository.save(announcement);

        return AnnouncementCreateResponse.from(announcement);
    }
    // 게시글 조회

    //  private String breed; // 견종
    //    private String image; // 강아지 이미지
    //    private boolean status;  // 입양 상태
    //    private Long Id; // 공고번호
    public List<AnnouncementListResponse> getAllAnnouncements() {
        List<Announcement> announcements = announcementRepository.findAll();
        return announcements.stream()
                .map(a -> new AnnouncementListResponse(
                        a.getPet().getBreed(),
                        a.getImageUrl(),
                        a.getStatus() == AnnouncementStatus.ACTIVE,
                        a.getId()
                ))
                .collect(Collectors.toList());
    }

    public Announcement findById(Long announcementId) {
        return announcementRepository.findById(announcementId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "해당 공지사항을 찾을 수 없습니다. id=" + announcementId));
    }
}
