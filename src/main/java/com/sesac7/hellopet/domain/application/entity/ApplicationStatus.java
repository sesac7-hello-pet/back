package com.sesac7.hellopet.domain.application.entity;

import lombok.Getter;

@Getter
public enum ApplicationStatus {
    PENDING("대기"),
    APPROVED("승인"),
    REJECTED("거절"),
    CANCELLED("취소");

    private final String label;

    ApplicationStatus(String label) {
        this.label = label;
    }
}
