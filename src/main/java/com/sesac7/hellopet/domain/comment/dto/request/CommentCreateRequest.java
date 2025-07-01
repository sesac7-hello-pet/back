package com.sesac7.hellopet.domain.comment.dto.request;

import com.sesac7.hellopet.domain.board.entity.Board;
import com.sesac7.hellopet.domain.comment.entity.Comment;
import com.sesac7.hellopet.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CommentCreateRequest {
    @NotBlank
    private String content;

    public Comment toDomain(Board board, User user) {
        return Comment.builder()
                .content(content)
                .createdAt(LocalDateTime.now())
                .board(board)
                .user(user)
                .build();
    }

}
