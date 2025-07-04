package com.sesac7.hellopet.domain.announcement.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
// 상세 페이지용 응답 dto
public class AnnouncementDetailResponse {
    private String id;
    private String breed;
    private String gender;
    private String health;
    private String personality;
    private int age;
    private String shelterName; // 보호소
    private LocalDateTime createdAt;
    private LocalDateTime announcementPeriod;
    private String imageUrl; // 강아지 이미지
}
