package com.sesac7.hellopet.domain.application.repository;

import com.sesac7.hellopet.domain.application.entity.Application;
import com.sesac7.hellopet.domain.application.entity.ApplicationStatus;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    @EntityGraph(attributePaths = {"announcement"})
    Page<Application> findByApplicantId(Long applicantId, Pageable pageable);

    @EntityGraph(attributePaths = {"applicant", "applicant.userDetail"})
    Page<Application> findByAnnouncementId(Long announcementId, Pageable pageable);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("""
                UPDATE Application a
                SET a.status = 'REJECTED'
                WHERE a.announcement.id = :announcementId
                AND a.id != :applicationId
                AND a.status = 'PENDING'
            """)
    int bulkRejectApplications(@Param("announcementId") Long announcementId,
                               @Param("applicationId") Long applicationId);

    Optional<Application> findByIdAndAnnouncementIdAndStatus(Long applicationId, Long announcementId,
                                                             ApplicationStatus status);

    Optional<Application> findByApplicantIdAndAnnouncementId(Long userId, Long announcementId);
}
