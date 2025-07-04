package com.sesac7.hellopet.domain.announcement.repository;

import com.sesac7.hellopet.domain.announcement.dto.response.AnnouncementListResponse;
import com.sesac7.hellopet.domain.announcement.entity.Announcement;
import com.sesac7.hellopet.domain.announcement.entity.AnnouncementStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

    // IN_PROGRESS 상태인 공고만 조회
    List<Announcement> findByStatus(AnnouncementStatus status);

    // 특정 동물 ID 기준으로 조회
    Optional<Announcement> findById(Long petId);

    // shelter 기준으로 조회할 수도 있음 (필요시)
    List<Announcement> findByShelterId(Long shelterId);


    @Query("""
SELECT NEW com.sesac7.hellopet.domain.announcement.dto.response.AnnouncementListResponse(
p.breed, p.imageUrl, (a.status), a.id, ud.username, a.createdAt
) from Announcement a 
join a.pet p 
join a.shelter s 
join s.userDetail ud 
where a.status = :s
""")
    Page<AnnouncementListResponse> searchAnnouncements(AnnouncementStatus s, Pageable pageable);

    @Query("""
SELECT NEW com.sesac7.hellopet.domain.announcement.dto.response.AnnouncementListResponse(
p.breed, p.imageUrl, (a.status), a.id, ud.username, a.createdAt
) from Announcement a 
join a.pet p 
join a.shelter s 
join s.userDetail ud 
where s.email = :email
""")
    Page<AnnouncementListResponse> searchMyAnnouncement(@Param("email") String email, Pageable pageable);
}
