package com.sesac7.hellopet.domain.application.entity;

import com.sesac7.hellopet.domain.announcement.entity.Announcement;
import com.sesac7.hellopet.domain.application.dto.request.ApplicationCreateRequest;
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
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User applicant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "announcement_id", nullable = false)
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
    private ApplicationStatus status;

    @CreationTimestamp
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

    public static Application of(User user, Announcement announcement, ApplicationCreateRequest request) {
        return Application.builder()
                          .user(user)
                          .announcement(announcement)
                          .reason(request.getReason())
                          .housingInfo(HousingInfo.from(request.getHousingInfo()))
                          .familyInfo(FamilyInfo.from(request.getFamilyInfo()))
                          .careInfo(CareInfo.from(request.getCareInfo()))
                          .financialInfo(FinancialInfo.from(request.getFinancialInfo()))
                          .petExperienceInfo(PetExperienceInfo.from(request.getPetExperienceInfo()))
                          .futurePlanInfo(FuturePlanInfo.from(request.getFuturePlanInfo()))
                          .agreementInfo(AgreementInfo.from(request.getAgreement()))
                          .build();
    }

    public void completeProcessing(ApplicationStatus newStatus) {
        this.status = newStatus;
        this.processedAt = LocalDateTime.now();
    }
}
