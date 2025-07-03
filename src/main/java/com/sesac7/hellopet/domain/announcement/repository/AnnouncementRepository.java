package com.sesac7.hellopet.domain.announcement.repository;

import com.sesac7.hellopet.domain.announcement.entity.Announcement;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

    // 전체 게시글 목록
    List<Announcement> findAll();

    // 특정 동물 ID 기준으로 조회
    Optional<Announcement> findById(Long petId);

    // shelter 기준으로 조회할 수도 있음 (필요시)
    List<Announcement> findByShelterId(Long shelterId);

    List<Announcement> findByShelter_UserDetail_User_Email(String email);
}
