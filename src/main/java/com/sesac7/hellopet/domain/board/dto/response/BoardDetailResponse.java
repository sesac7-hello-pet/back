package com.sesac7.hellopet.domain.board.dto.response;

import com.sesac7.hellopet.domain.board.entity.BoardCategory;
import com.sesac7.hellopet.domain.board.entity.PetType;
import com.sesac7.hellopet.domain.comment.entity.Comment;
import java.time.LocalDateTime;
import java.util.List;

public class BoardDetailResponse {
    private Long id;
    private String nickname;
    private String title;
    private String content;
    private String img_url;
    private BoardCategory boardCategory;
    private PetType petType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int likesCount;
    private int viewsCount;
    private int commentsCount;
    private List<Comment> commentList;
}
