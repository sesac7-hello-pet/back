package com.sesac7.hellopet.domain.application.dto.response;

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
}
