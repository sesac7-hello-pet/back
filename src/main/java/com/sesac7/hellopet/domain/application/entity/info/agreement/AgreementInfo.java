package com.sesac7.hellopet.domain.application.entity.info.agreement;

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
}
