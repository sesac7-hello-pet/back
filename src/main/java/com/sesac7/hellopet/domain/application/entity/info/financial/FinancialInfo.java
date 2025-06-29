package com.sesac7.hellopet.domain.application.entity.info.financial;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FinancialInfo {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MonthlyBudget monthlyBudget;

    @Column(nullable = false)
    private boolean hasEmergencyFund;
}

