package com.sesac7.hellopet.domain.application.entity.environment;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Environment {

    @Embedded
    private HousingInfo housing;

    @Embedded
    private FamilyInfo family;

    @Embedded
    private DailyRoutineInfo dailyRoutine;

    @Embedded
    private FinancialInfo financial;

    @Embedded
    private PetExperienceInfo petExperience;

    @Embedded
    private PlansInfo plans;

    private boolean checklistConfirmed;

    @Builder
    public Environment(HousingInfo housing, FamilyInfo family, DailyRoutineInfo dailyRoutine,
                       FinancialInfo financial, PetExperienceInfo petExperience,
                       PlansInfo plans, boolean checklistConfirmed) {
        this.housing = housing;
        this.family = family;
        this.dailyRoutine = dailyRoutine;
        this.financial = financial;
        this.petExperience = petExperience;
        this.plans = plans;
        this.checklistConfirmed = checklistConfirmed;
    }
}
