package com.sesac7.hellopet.domain.application.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class ApplicationPageRequest {
    @Min(0)
    private int page = 0;

    @Min(1)
    @Max(100)
    private int size = 10;

    public Pageable toPageable() {
        return PageRequest.of(page, size);
    }
}
