package com.sesac7.hellopet.domain.application.dto.response;

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
}
