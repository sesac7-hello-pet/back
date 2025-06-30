package com.sesac7.hellopet.domain.application.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApplicationResponse {
    private Long applicationId;
    private String message;
}
