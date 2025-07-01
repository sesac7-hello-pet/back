package com.sesac7.hellopet.domain.application.dto.response;

import com.sesac7.hellopet.domain.announcement.entity.Announcement;
import com.sesac7.hellopet.domain.application.entity.Application;
import com.sesac7.hellopet.domain.user.entity.UserDetail;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ShelterApplicationResponse {
    private Long announcementId;
    private LocalDateTime announcementCreatedAt;
    private String userName;
    private String userPhoneNumber;
    private String userEmail;

    public static ShelterApplicationResponse of(Announcement announcement, Application application) {
        UserDetail userDetail = application.getApplicant().getUserDetail();
        return ShelterApplicationResponse.builder()
                                         .announcementId(announcement.getId())
                                         .announcementCreatedAt(announcement.getCreateAt())
                                         .userName(userDetail.getUsername())
                                         .userPhoneNumber(userDetail.getPhoneNumber())
                                         .userEmail(application.getApplicant().getEmail())
                                         .build();
    }
}
