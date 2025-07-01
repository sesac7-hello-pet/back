package com.sesac7.hellopet.domain.application.repository;

import com.sesac7.hellopet.domain.application.entity.Application;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    Page<Application> findByApplicantIdOrderBySubmittedAtDesc(Long applicantId, Pageable pageable);

    Page<Application> findByAnnouncementIdOrderBySubmittedAtDesc(Long announcementId, Pageable pageable);
}
