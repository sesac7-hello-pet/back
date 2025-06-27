package com.sesac7.hellopet.domain.application.entity.environment;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FinancialInfo {

    private int monthlyBudget;
    private boolean hasEmergencyFund;

    @Builder
    public FinancialInfo(int monthlyBudget, boolean hasEmergencyFund) {
        this.monthlyBudget = monthlyBudget;
        this.hasEmergencyFund = hasEmergencyFund;
    }
}
