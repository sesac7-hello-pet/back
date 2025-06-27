package com.sesac7.hellopet.domain.application.entity.environment;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PetExperienceInfo {

    private boolean hasPetExperience;

    @Column(columnDefinition = "TEXT")
    private String pastPetExperienceDetail;

    @Builder
    public PetExperienceInfo(boolean hasPetExperience, String pastPetExperienceDetail) {
        this.hasPetExperience = hasPetExperience;
        this.pastPetExperienceDetail = pastPetExperienceDetail;
    }
}
