package com.sesac7.hellopet.domain.application.dto.response;

import com.sesac7.hellopet.domain.application.entity.Application;
import com.sesac7.hellopet.domain.user.entity.UserDetail;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ShelterApplicationResponse {
    private Long applicationId;
    private String applicationStatusLabel;
    private String userName;
    private String userPhoneNumber;
    private String userEmail;

    public static ShelterApplicationResponse from(Application application) {
        UserDetail userDetail = application.getApplicant().getUserDetail();
        return ShelterApplicationResponse.builder()
                                         .applicationId(application.getId())
                                         .applicationStatusLabel(application.getStatus().getLabel())
                                         .userName(userDetail.getUsername())
                                         .userPhoneNumber(userDetail.getPhoneNumber())
                                         .userEmail(application.getApplicant().getEmail())
                                         .build();
    }
}
