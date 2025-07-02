package com.sesac7.hellopet.domain.application.entity.info.care;

import com.sesac7.hellopet.domain.application.dto.request.CareInfoRequest;
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
public class CareInfo {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AbsenceTime absenceTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CareTime careTime;

    public static CareInfo from(CareInfoRequest request) {
        return new CareInfo(
                request.getAbsenceTime(),
                request.getCareTime()
        );
    }
}
