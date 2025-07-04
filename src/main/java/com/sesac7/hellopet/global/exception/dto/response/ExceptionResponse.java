package com.sesac7.hellopet.global.exception.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExceptionResponse {
    private static final String DEFAULT_ERROR_MESSAGE = "처리 중 오류가 발생했습니다. 관리자에게 문의하세요.";

    private String exceptionName;
    private String message;
    private int httpStatusCode;
    private String httpStatusName;
    private LocalDateTime createdAt;

    public static ExceptionResponse of(Exception e, int statusCode, String statusName) {
        return ExceptionResponse.builder()
                                .exceptionName(e.getClass().getSimpleName())
                                .message(e.getMessage() != null ? e.getMessage() : DEFAULT_ERROR_MESSAGE)
                                .httpStatusCode(statusCode)
                                .httpStatusName(statusName)
                                .createdAt(LocalDateTime.now())
                                .build();
    }
}
