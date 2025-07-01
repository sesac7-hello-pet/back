package com.sesac7.hellopet.domain.comment.dto.response;

import java.time.LocalDateTime;

public class BoardCommentResponse {
    private Long id;
    private String nickname;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
