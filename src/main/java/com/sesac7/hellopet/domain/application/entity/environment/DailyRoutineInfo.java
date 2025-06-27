package com.sesac7.hellopet.domain.application.entity.environment;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailyRoutineInfo {

    private int absenceHoursPerDay;
    private int availableTimeForPet;

    @Builder
    public DailyRoutineInfo(int absenceHoursPerDay, int availableTimeForPet) {
        this.absenceHoursPerDay = absenceHoursPerDay;
        this.availableTimeForPet = availableTimeForPet;
    }
}
