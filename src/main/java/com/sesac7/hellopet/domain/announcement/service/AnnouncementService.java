package com.sesac7.hellopet.domain.announcement.service;

import com.sesac7.hellopet.common.utils.CustomUserDetails;
import com.sesac7.hellopet.domain.announcement.dto.request.AnnouncementCreateRequest;
import com.sesac7.hellopet.domain.announcement.dto.request.AnnouncementSearchRequest;
import com.sesac7.hellopet.domain.announcement.dto.request.AnnouncementUpdateRequest;
import com.sesac7.hellopet.domain.announcement.dto.response.AnnouncementCreateResponse;
import com.sesac7.hellopet.domain.announcement.dto.response.AnnouncementDetailResponse;
import com.sesac7.hellopet.domain.announcement.dto.response.AnnouncementListResponse;
import com.sesac7.hellopet.domain.announcement.dto.response.AnnouncementPageResponse;
import com.sesac7.hellopet.domain.announcement.entity.Announcement;
import com.sesac7.hellopet.domain.announcement.entity.AnnouncementStatus;
import com.sesac7.hellopet.domain.announcement.entity.Pet;
import com.sesac7.hellopet.domain.announcement.repository.AnnouncementRepository;
import com.sesac7.hellopet.domain.announcement.repository.PetRepository;
import com.sesac7.hellopet.domain.application.service.ApplicationService;
import com.sesac7.hellopet.domain.user.entity.User;
import com.sesac7.hellopet.domain.user.service.UserFinder;
import com.sesac7.hellopet.global.annotation.IsAnnouncementOwner;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AnnouncementService {
    private final PetRepository petRepository;
    private final AnnouncementRepository announcementRepository;
    private final UserFinder userFinder;
    private final ApplicationService applicationService;

    /***
     * 게시글 등록
     * @param announcementCreateRequest
     * @param customUserDetails
     * @return
     */
    public AnnouncementCreateResponse createAnnouncement(AnnouncementCreateRequest announcementCreateRequest,
                                                         CustomUserDetails customUserDetails) {
        Pet pet = Pet.builder()
                     .animalType(announcementCreateRequest.getAnimalType())
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
                                                .announcementPeriod(announcementCreateRequest.getSelectedDate())
                                                .createdAt(LocalDateTime.now())
                                                .updatedAt(null)
                                                .build();

        announcementRepository.save(announcement);

        return AnnouncementCreateResponse.from(announcement);
    }

    /***
     * 게시글 전체리스트 조회
     * @return AnnouncementListResponse 리스트
     */
    @Transactional(readOnly = true)
    public AnnouncementPageResponse getAllAnnouncements(AnnouncementSearchRequest request) {
        Page<AnnouncementListResponse> announcements = announcementRepository.searchAnnouncements(
                AnnouncementStatus.IN_PROGRESS, request.toPageable());

        return AnnouncementPageResponse.from(announcements, request);

    }

    // 특정 공지사항 ID로 Announcement 엔터티를 조회하는 메서드
    public Announcement findById(Long announcementId) {
        return announcementRepository.findById(announcementId)
                                     .orElseThrow(() -> new EntityNotFoundException(
                                             "해당 입양 공고를 찾을 수 없습니다. id=" + announcementId));
    }

    /**
     * 공고 ID로 상세 입양 공고 정보를 조회하는 메서드
     *
     * @param id          조회할 입양 공고의 고유 ID
     * @param userDetails 로그인 사용자 정보 (null 가능)
     * @return 상세 입양 공고 정보 DTO
     * @throws NoSuchElementException 해당 ID의 공고가 없을 경우 예외 발생
     */
    @Transactional(readOnly = true)
    public AnnouncementDetailResponse getAnnouncementDetail(Long id, CustomUserDetails userDetails) {
        // 공고 ID로 Announcement 엔티티 조회
        Announcement announcement = announcementRepository.findById(id)
                                                          .orElseThrow(
                                                                  () -> new NoSuchElementException("입양 공고가 없습니다."));

        Pet pet = announcement.getPet();

        // 로그인 사용자가 해당 공고에 이미 신청했는지 여부 확인
        boolean alreadyApplied = false;
        if (userDetails != null) {
            User user = userFinder.findLoggedInUserByUsername(userDetails.getUsername());
            alreadyApplied = applicationService.existsByAnnouncementIdAndApplicantId(
                    announcement.getId(), user.getId());
        }

        // 상세 응답 DTO 생성 및 반환
        return AnnouncementDetailResponse.builder()
                                         .id(String.valueOf(announcement.getId()))
                                         .breed(pet.getBreed())
                                         .gender(pet.getGender())
                                         .health(pet.getHealth())
                                         .personality(pet.getPersonality())
                                         .shelterName(announcement.getShelter().getUserDetail().getNickname())
                                         .createdAt(announcement.getCreatedAt())
                                         .announcementPeriod(announcement.getAnnouncementPeriod())
                                         .imageUrl(pet.getImageUrl())
                                         .announcementStatus(announcement.getStatus())
                                         .animalType(pet.getAnimalType())
                                         .alreadyApplied(alreadyApplied)
                                         .build();
    }

    /***
     * 게시글 수정(update)
     */
    @IsAnnouncementOwner
    public AnnouncementUpdateRequest updateAnnouncement(
            Long announcementId,
            AnnouncementUpdateRequest announcementUpdateRequest,
            String username) throws Exception {

        // 1. 게시글 조회
        Announcement announcement = announcementRepository.findById(announcementId)
                                                          .orElseThrow(() -> new EntityNotFoundException(
                                                                  "입양 공고가 존재하지 않습니다."));

//        if (!announcement.getShelter().getEmail().equals(username)) {
//            throw new Exception("수정권한이 없습니다");
//        }

        // 3. Pet 수정 (updateInfo 메서드로 대체 권장)
        Pet pet = announcement.getPet();
        pet.updateInfo(
                announcementUpdateRequest.getBreed(),
                announcementUpdateRequest.getGender(),
                announcementUpdateRequest.getAge(),
                announcementUpdateRequest.getHealth(),
                announcementUpdateRequest.getPersonality(),
                announcementUpdateRequest.getImage(),
                announcementUpdateRequest.getAnimalType()
        );

        // 4. 수정일 갱신
        announcement.getUpdatedAt();  // announcement.setUpdateAt(LocalDateTime.now()); 대신

        // 5. 요청 DTO 그대로 반환 (필요하면 Response DTO로 변경 권장)
        return announcementUpdateRequest;
    }

    /***
     * 게시글 삭제
     * @param announcementId 삭제할 공고의 ID
     */
    @IsAnnouncementOwner
    public void deleteAnnouncement(Long announcementId, String username) {
        Announcement announcement = announcementRepository.findById(announcementId)
                                                          .orElseThrow(() -> new EntityNotFoundException(
                                                                  "삭제할 공고가 존재하지 않습니다."));

//        // 🔐 작성자 확인
//        if (!announcement.getShelter().getUserDetail().getUser().getEmail().equals(username)) {
//            throw new AccessDeniedException("삭제 권한이 없습니다.");
//        }

        announcementRepository.delete(announcement);
    }

    /***
     * 내가 쓴 입양 공고 조회
     */
    @Transactional(readOnly = true)
    public AnnouncementPageResponse getMyAnnouncements(String email, Pageable pageable) {
        Page<AnnouncementListResponse> announcementListResponses = announcementRepository.searchMyAnnouncement(email,
                pageable);

        AnnouncementSearchRequest searchRequest = new AnnouncementSearchRequest();
        return AnnouncementPageResponse.from(announcementListResponses, searchRequest);
    }

    public void completeAnnouncement(Long id) {
        Announcement announcement = findById(id);
        announcement.changeStatus(AnnouncementStatus.COMPLETED);
    }
}
