package com.sesac7.hellopet.domain.application.dto.response.detail;

import com.sesac7.hellopet.domain.application.entity.info.financial.FinancialInfo;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FinancialInfoResponse {
    private String monthlyBudget;
    private String monthlyBudgetLabel;
    private boolean hasEmergencyFund;

    public static FinancialInfoResponse from(FinancialInfo financialInfo) {
        return FinancialInfoResponse.builder()
                                    .monthlyBudget(financialInfo.getMonthlyBudget().name())
                                    .monthlyBudgetLabel(financialInfo.getMonthlyBudget().getLabel())
                                    .hasEmergencyFund(financialInfo.isHasEmergencyFund())
                                    .build();
    }
}
