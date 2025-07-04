package com.sesac7.hellopet.domain.application.entity.info.experience;

import com.sesac7.hellopet.domain.application.dto.request.PetExperienceInfoRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PetExperienceInfo {

    @Column(nullable = false)
    private boolean hasPetExperience;

    @Column(columnDefinition = "TEXT")
    private String experienceDetails;

    public static PetExperienceInfo from(PetExperienceInfoRequest request) {
        return new PetExperienceInfo(
                request.getHasPetExperience(),
                request.getExperienceDetails()
        );
    }
}
