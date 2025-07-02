package com.sesac7.hellopet.domain.comment.dto.request;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CommentSearchRequest {
    private String email;
    private int page = 0;
    private int size = 0;
}
