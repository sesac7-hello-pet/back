package com.sesac7.hellopet.domain.user.dto.response;

import com.sesac7.hellopet.domain.user.dto.request.CheckField;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExistResponse {
    private CheckField field;
    private String value;
    private boolean result;
    private String message;
}
