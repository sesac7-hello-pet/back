package com.sesac7.hellopet.global.exception.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorMessage {
    private String error;
    private String message;
}
