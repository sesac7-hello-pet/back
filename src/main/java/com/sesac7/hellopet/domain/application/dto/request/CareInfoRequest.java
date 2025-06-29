package com.sesac7.hellopet.domain.application.dto.request;

import com.sesac7.hellopet.domain.application.entity.info.care.AbsenceTime;
import com.sesac7.hellopet.domain.application.entity.info.care.CareTime;
import lombok.Getter;

@Getter
public class CareInfoRequest {
    private AbsenceTime absenceTime;
    private CareTime careTime;
}
