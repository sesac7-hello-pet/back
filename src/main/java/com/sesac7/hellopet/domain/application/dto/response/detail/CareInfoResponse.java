package com.sesac7.hellopet.domain.application.dto.response.detail;

import com.sesac7.hellopet.domain.application.entity.info.care.CareInfo;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CareInfoResponse {
    private String absenceTime;
    private String absenceTimeLabel;
    private String careTime;
    private String careTimeLabel;

    public static CareInfoResponse from(CareInfo careInfo) {
        return CareInfoResponse.builder()
                               .absenceTime(careInfo.getAbsenceTime().name())
                               .absenceTimeLabel(careInfo.getAbsenceTime().getLabel())
                               .careTime(careInfo.getCareTime().name())
                               .careTimeLabel(careInfo.getCareTime().getLabel())
                               .build();
    }
}
