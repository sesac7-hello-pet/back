package com.sesac7.hellopet.domain.comment.dto.request;

import com.sesac7.hellopet.domain.user.entity.User;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Getter
public class BoardCommentCreateRequest {
    private String content;

    @ManyToOne
    private User user;

}
