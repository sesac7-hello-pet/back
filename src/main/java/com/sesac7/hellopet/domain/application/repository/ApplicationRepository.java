package com.sesac7.hellopet.domain.application.repository;

import com.sesac7.hellopet.domain.application.entity.Application;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    @Query("""
            SELECT app FROM Application app
            JOIN FETCH app.announcement
            WHERE app.applicant.id = :applicantId
            ORDER BY app.submittedAt DESC
            """)
    List<Application> findApplicationsWithAnnouncementByApplicantId(@Param("applicantId") Long applicantId);

    @Query("""
                SELECT app FROM Application app
                JOIN FETCH app.applicant user
                JOIN FETCH user.userDetail
                WHERE app.announcement.id = :announcementId
                ORDER BY app.submittedAt DESC
            """)
    List<Application> findApplicationsWithUserDetailByAnnouncementId(@Param("announcementId") Long announcementId);

    @Query("""
                SELECT app FROM Application app
                WHERE app.announcement.id = :announcementId
                AND app.id != :applicationId
            """)
    List<Application> findByAnnouncementIdAndExcludeApplicationId(@Param("announcementId") Long announcementId,
                                                                  @Param("applicationId") Long applicationId);
}
