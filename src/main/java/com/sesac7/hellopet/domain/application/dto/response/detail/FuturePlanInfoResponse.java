package com.sesac7.hellopet.domain.application.dto.response.detail;

import com.sesac7.hellopet.domain.application.entity.info.plan.FuturePlanInfo;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FuturePlanInfoResponse {
    private boolean hasFuturePlan;
    private String planDetails;

    public static FuturePlanInfoResponse from(FuturePlanInfo futurePlanInfo) {
        return FuturePlanInfoResponse.builder()
                                     .hasFuturePlan(futurePlanInfo.isHasFuturePlan())
                                     .planDetails(futurePlanInfo.getPlanDetails())
                                     .build();
    }
}
