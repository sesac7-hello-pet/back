package com.sesac7.hellopet.domain.application.entity.info.plan;

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
public class FuturePlanInfo {

    @Column(nullable = false)
    private boolean hasFuturePlan;

    @Column(columnDefinition = "TEXT")
    private String planDetails;
}
