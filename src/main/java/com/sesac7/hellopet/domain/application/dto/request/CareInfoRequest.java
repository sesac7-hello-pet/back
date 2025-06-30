package com.sesac7.hellopet.domain.application.dto.request;

import com.sesac7.hellopet.domain.application.entity.info.care.AbsenceTime;
import com.sesac7.hellopet.domain.application.entity.info.care.CareTime;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CareInfoRequest {

    @NotNull(message = "부재 시간은 필수 입력 항목입니다.")
    private AbsenceTime absenceTime;

    @NotNull(message = "돌봄 시간은 필수 입력 항목입니다.")
    private CareTime careTime;
}
