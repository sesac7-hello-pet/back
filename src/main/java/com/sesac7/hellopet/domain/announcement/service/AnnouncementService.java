package com.sesac7.hellopet.domain.announcement.service;

import com.sesac7.hellopet.common.utils.CustomUserDetails;
import com.sesac7.hellopet.domain.announcement.dto.request.AnnouncementCreateRequest;
import com.sesac7.hellopet.domain.announcement.dto.response.AnnouncementCreateResponse;
import com.sesac7.hellopet.domain.announcement.dto.response.AnnouncementDetailResponse;

import com.sesac7.hellopet.domain.announcement.dto.response.AnnouncementListResponse;
import com.sesac7.hellopet.domain.announcement.entity.Announcement;
import com.sesac7.hellopet.domain.announcement.entity.AnnouncementStatus;
import com.sesac7.hellopet.domain.announcement.entity.Pet;
import com.sesac7.hellopet.domain.announcement.repository.AnnouncementRepository;
import com.sesac7.hellopet.domain.announcement.repository.PetRepository;
import com.sesac7.hellopet.domain.user.service.UserFinder;
import com.sesac7.hellopet.domain.user.entity.User;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
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

    /***
     * 게시글 등록
     * @param announcementCreateRequest
     * @param customUserDetails
     * @return
     */
    public AnnouncementCreateResponse createAnnouncement(AnnouncementCreateRequest announcementCreateRequest,
                                                         CustomUserDetails customUserDetails) {
        Pet pet = Pet.builder()
                .breed(announcementCreateRequest.getBreed())
                .gender(announcementCreateRequest.getGender())
                .age(announcementCreateRequest.getAge())
                .health(announcementCreateRequest.getHealth())
                .personality(announcementCreateRequest.getPersonality())
                .foundPlace("발견 장소")
                .imageUrl(announcementCreateRequest.getImage())  // Pet에 이미지 저장하도록 추가
                .build();

        petRepository.save(pet);

        User shelter = userFinder.findLoggedInUserByUsername(customUserDetails.getUsername());

        Announcement announcement = Announcement.builder()
                .shelter(shelter)
                .pet(pet)
                .status(AnnouncementStatus.IN_PROGRESS)
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();

        announcementRepository.save(announcement);

        return AnnouncementCreateResponse.from(announcement);
    }

    /***
     * 게시글 전체리스트 조회
     * @return AnnouncementListResponse 리스트
     */

    public List<AnnouncementListResponse> getAllAnnouncements() {
        List<Announcement> announcements = announcementRepository.findAll();

        return announcements.stream()
                .map(a -> new AnnouncementListResponse(
                        a.getPet().getBreed(),            // Pet 품종
                        a.getPet().getImageUrl(),         // Pet 이미지 URL로 변경

                        a.getStatus() == AnnouncementStatus.ACTIVE,
                        a.getId()
                ))
                .collect(Collectors.toList());
    }

    // 특정 공지사항 ID로 Announcement 엔터티를 조회하는 메서드
    public Announcement findById(Long announcementId) {
        return announcementRepository.findById(announcementId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "해당 공지사항을 찾을 수 없습니다. id=" + announcementId));
    }


    /***
     * 게시글 상세 조회
     */
    /**
     * 공고 ID로 상세 입양 공고 정보를 조회하는 메서드
     *
     * @param id 조회할 입양 공고의 고유 ID
     * @return AnnouncementDetailResponse DTO - 상세 입양 공고 정보
     * @throws NoSuchElementException 해당 ID의 입양 공고가 없으면 예외 발생
     */
    public AnnouncementDetailResponse getAnnouncementDetail(Long id) {
        // announcementRepository를 통해 DB에서 해당 ID의 Announcement 엔터티를 조회한다.
        // 조회 결과가 없으면 NoSuchElementException 예외를 던진다.
        Announcement announcement = announcementRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("입양 공고가 없습니다."));

        // 조회된 Announcement 엔터티에 연결된 Pet 엔터티를 가져온다.
        Pet pet = announcement.getPet();

        // AnnouncementDetailResponse DTO 빌더를 사용하여 상세 정보 객체를 생성한다.
        return AnnouncementDetailResponse.builder()
                .id(String.valueOf(announcement.getId())) // 공고 ID를 문자열로 변환하여 저장
                .breed(pet.getBreed())                     // 펫의 품종 정보 설정
                .gender(pet.getGender())                   // 펫의 성별 정보 설정
                .health(pet.getHealth())                   // 펫의 건강 상태 정보 설정
                .personality(pet.getPersonality())         // 펫의 성격 정보 설정
                .shelterName(announcement.getShelter().getUserDetail().getNickname())
                .imageUrl(pet.getImageUrl())               // 펫의 이미지 URL 설정
                .build();                                  // DTO 객체 생성 및 반환
    }

}
