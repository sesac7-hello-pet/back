package com.sesac7.hellopet.domain.application.entity.info.agreement;

import com.sesac7.hellopet.domain.application.dto.request.AgreementInfoRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AgreementInfo {

    @Column(nullable = false)
    private Boolean agreedToAccuracy;

    @Column(nullable = false)
    private Boolean agreedToCare;

    @Column(nullable = false)
    private Boolean agreedToPrivacy;

    public static AgreementInfo from(AgreementInfoRequest request) {
        return new AgreementInfo(
                request.getAgreedToAccuracy(),
                request.getAgreedToCare(),
                request.getAgreedToPrivacy()
        );
    }
}
