package com.sesac7.hellopet.domain.application.validation;

public class AnnouncementApprovalPermissionException extends RuntimeException {
    public AnnouncementApprovalPermissionException() {
        super("해당 입양 공고에 대한 승인 권한이 없습니다.");
    }
}
