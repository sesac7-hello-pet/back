package com.sesac7.hellopet.domain.application.dto.request;

import com.sesac7.hellopet.domain.application.entity.info.financial.MonthlyBudget;
import lombok.Getter;

@Getter
public class FinancialInfoRequest {
    private MonthlyBudget monthlyBudget;
    private boolean hasEmergencyFund;
}
