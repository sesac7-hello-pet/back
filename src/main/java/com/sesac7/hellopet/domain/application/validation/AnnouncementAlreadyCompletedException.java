package com.sesac7.hellopet.domain.application.validation;

public class AnnouncementAlreadyCompletedException extends RuntimeException {
    public AnnouncementAlreadyCompletedException(Long id) {
        super("이미 마감된 입양 공고입니다. id=" + id);
    }
}

