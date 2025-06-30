package com.sesac7.hellopet.domain.application.dto.request;

import jakarta.validation.constraints.AssertTrue;
import lombok.Getter;

@Getter
public class AgreementInfoRequest {

    @AssertTrue(message = "사실에 근거하여 작성했음을 동의해야 합니다.")
    private Boolean agreedToAccuracy;

    @AssertTrue(message = "책임감 있는 돌봄 약속에 동의해야 합니다.")
    private Boolean agreedToCare;

    @AssertTrue(message = "개인정보 수집 및 활용에 동의해야 합니다.")
    private Boolean agreedToPrivacy;
}
