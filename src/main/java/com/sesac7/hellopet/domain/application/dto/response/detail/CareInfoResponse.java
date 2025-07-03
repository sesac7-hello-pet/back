package com.sesac7.hellopet.domain.application.dto.response.detail;

import com.sesac7.hellopet.domain.application.entity.info.care.CareInfo;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CareInfoResponse {
    private String absenceTime;
    private String careTime;

    public static CareInfoResponse from(CareInfo careInfo) {
        return CareInfoResponse.builder()
                               .absenceTime(careInfo.getAbsenceTime().name())
                               .careTime(careInfo.getCareTime().name())
                               .build();
    }
}
