package com.sesac7.hellopet.domain.announcement.dto.response;

// 전체 등록된 동물 목록 응답 dto
public class AnnoucementListDTO {
    private String breed; // 견종
    private String image; // 강아지 이미지
    private boolean status;  // 입양 상태
    private Long announcementId; // 공고번호
}
