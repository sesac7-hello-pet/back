package com.sesac7.hellopet.domain.application.entity;

import com.sesac7.hellopet.domain.announcement.entity.Announcement;
import com.sesac7.hellopet.domain.application.entity.info.agreement.AgreementInfo;
import com.sesac7.hellopet.domain.application.entity.info.care.CareInfo;
import com.sesac7.hellopet.domain.application.entity.info.experience.PetExperienceInfo;
import com.sesac7.hellopet.domain.application.entity.info.family.FamilyInfo;
import com.sesac7.hellopet.domain.application.entity.info.financial.FinancialInfo;
import com.sesac7.hellopet.domain.application.entity.info.housing.HousingInfo;
import com.sesac7.hellopet.domain.application.entity.info.plan.FuturePlanInfo;
import com.sesac7.hellopet.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "applications")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User applicant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "announcement_id", nullable = false)
    @Getter
    private Announcement announcement;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String reason;

    @Embedded
    private HousingInfo housingInfo;

    @Embedded
    private FamilyInfo familyInfo;

    @Embedded
    private CareInfo careInfo;

    @Embedded
    private FinancialInfo financialInfo;

    @Embedded
    private PetExperienceInfo petExperienceInfo;

    @Embedded
    private FuturePlanInfo futurePlanInfo;

    @Embedded
    private AgreementInfo agreementInfo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Getter
    private ApplicationStatus status;

    @CreationTimestamp
    @Getter
    private LocalDateTime submittedAt;
    private LocalDateTime processedAt;

    @Builder
    public Application(User user, Announcement announcement, String reason, HousingInfo housingInfo,
                       FamilyInfo familyInfo,
                       CareInfo careInfo, FinancialInfo financialInfo, PetExperienceInfo petExperienceInfo,
                       FuturePlanInfo futurePlanInfo, AgreementInfo agreementInfo, ApplicationStatus status) {
        this.applicant = user;
        this.announcement = announcement;
        this.reason = reason;
        this.housingInfo = housingInfo;
        this.familyInfo = familyInfo;
        this.careInfo = careInfo;
        this.financialInfo = financialInfo;
        this.petExperienceInfo = petExperienceInfo;
        this.futurePlanInfo = futurePlanInfo;
        this.agreementInfo = agreementInfo;
        this.status = status != null ? status : ApplicationStatus.PENDING;
    }

    public void completeProcessing(ApplicationStatus newStatus) {
        this.status = newStatus;
        this.processedAt = LocalDateTime.now();
    }
}
