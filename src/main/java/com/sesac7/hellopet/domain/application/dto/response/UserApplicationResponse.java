package com.sesac7.hellopet.domain.application.dto.response;

import com.sesac7.hellopet.domain.announcement.entity.Announcement;
import com.sesac7.hellopet.domain.application.entity.Application;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserApplicationResponse {
    private Long applicationId;
    private Long announcementId;
    private String applicationStatusLabel;
    private LocalDateTime submittedAt;
    private String petImageUrl;

    public static UserApplicationResponse of(Application application, Announcement announcement) {
        return UserApplicationResponse.builder()
                                      .applicationId(application.getId())
                                      .announcementId(announcement.getId())
                                      .applicationStatusLabel(application.getStatus().name())
                                      .submittedAt(application.getSubmittedAt())
                                      .petImageUrl(announcement.getImageUrl())
                                      .build();
    }
}
