package com.sesac7.hellopet.domain.application.dto.response;

import com.sesac7.hellopet.domain.application.entity.ApplicationStatus;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApplicationApprovalResponse {
    private Long announcementId;
    private Long applicationId;
    private ApplicationStatus status;
    private LocalDateTime processedAt;
    private String message;

    public static ApplicationApprovalResponse of(Long announcementId, Long applicationId, ApplicationStatus status) {
        return ApplicationApprovalResponse.builder()
                                          .announcementId(announcementId)
                                          .applicationId(applicationId)
                                          .status(status)
                                          .processedAt(LocalDateTime.now())
                                          .message("승인이 완료되었습니다.")
                                          .build();
    }
}
