package com.sesac7.hellopet.domain.announcement.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Builder
// 동물 등록 시 클라이언트가 보내는 요청값(post 요청)
public class AnnouncementCreateRequest {
       // 공고번호 (문자열)
    private String breed;              // 견종
    private String gender;             // 성별
    private String health;             // 건강상태
    private String personality;        // 성격
    private int age;                   // 나이
    private int Date;                  // 공고 기간
    private String image;              // 이미지
}
