package com.sesac7.hellopet.domain.application.dto.response.detail;

import com.sesac7.hellopet.domain.application.entity.info.financial.FinancialInfo;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FinancialInfoResponse {
    private String monthlyBudget;
    private boolean hasEmergencyFund;

    public static FinancialInfoResponse from(FinancialInfo financialInfo) {
        return FinancialInfoResponse.builder()
                                    .monthlyBudget(financialInfo.getMonthlyBudget().name())
                                    .hasEmergencyFund(financialInfo.isHasEmergencyFund())
                                    .build();
    }
}
