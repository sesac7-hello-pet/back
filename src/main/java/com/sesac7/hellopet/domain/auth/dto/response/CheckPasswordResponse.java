package com.sesac7.hellopet.domain.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CheckPasswordResponse {
    private String message;
    private boolean pass;
}
