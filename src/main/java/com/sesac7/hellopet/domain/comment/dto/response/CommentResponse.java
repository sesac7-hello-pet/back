package com.sesac7.hellopet.domain.comment.dto.response;

import java.time.LocalDateTime;

public class CommentResponse {
    private Long id;
    private String nickname;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long boardId;
}
