package com.sesac7.hellopet.domain.application.dto.response.detail;

import com.sesac7.hellopet.domain.application.entity.info.experience.PetExperienceInfo;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PetExperienceInfoResponse {
    private boolean hasPetExperience;
    private String experienceDetails;

    public static PetExperienceInfoResponse from(PetExperienceInfo petExperienceInfo) {
        return PetExperienceInfoResponse.builder()
                                        .hasPetExperience(petExperienceInfo.isHasPetExperience())
                                        .experienceDetails(petExperienceInfo.getExperienceDetails())
                                        .build();
    }
}
