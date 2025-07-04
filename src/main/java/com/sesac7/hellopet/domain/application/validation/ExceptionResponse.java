package com.sesac7.hellopet.domain.application.validation;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExceptionResponse {
    private String exceptionName;
    private String message;
    private int httpStatusCode;
    private String HttpStatusName;
    private LocalDateTime createdAt;

    public static ExceptionResponse of(Exception e, int statusCode, String statusName) {
        return ExceptionResponse.builder()
                                .exceptionName(e.getClass().getSimpleName())
                                .message(e.getMessage())
                                .httpStatusCode(statusCode)
                                .HttpStatusName(statusName)
                                .createdAt(LocalDateTime.now())
                                .build();
    }
}
