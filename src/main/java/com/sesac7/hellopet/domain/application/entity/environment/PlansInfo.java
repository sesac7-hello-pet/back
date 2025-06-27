package com.sesac7.hellopet.domain.application.entity.environment;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlansInfo {

    private String plannedLivingSpace;
    private String hasUpcomingChanges;

    @Builder
    public PlansInfo(String plannedLivingSpace, String hasUpcomingChanges) {
        this.plannedLivingSpace = plannedLivingSpace;
        this.hasUpcomingChanges = hasUpcomingChanges;
    }
}
