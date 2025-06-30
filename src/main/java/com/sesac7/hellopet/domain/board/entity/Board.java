package com.sesac7.hellopet.domain.board.entity;

import com.sesac7.hellopet.domain.board.dto.response.BoardResponse;
import com.sesac7.hellopet.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "boards")
@NoArgsConstructor
@AllArgsConstructor
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 70)
    private String title;

    @Column(nullable = false)
    private String content;
    private String image_url;

    private int likesCount;
    private int viewsCount;
    private int commentsCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BoardCategory boardCategory;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PetType petType;

    @ManyToOne
    private User user;

    public BoardResponse toResponse() {
        BoardResponse boardResponse = new BoardResponse();
        boardResponse.setId(id);
        boardResponse.setNickname(user.getUserDetail().getNickname());
        boardResponse.setTitle(title);
        boardResponse.setContent(content);
        boardResponse.setImage_url(image_url);
        boardResponse.setLikesCount(likesCount);
        boardResponse.setViewsCount(viewsCount);
        boardResponse.setCommentsCount(commentsCount);
        boardResponse.setCreateAt(createdAt);
        boardResponse.setUpdateAt(updatedAt);
        boardResponse.setCategory(boardCategory);
        boardResponse.setPetType(petType);
        return boardResponse;
    }

}
