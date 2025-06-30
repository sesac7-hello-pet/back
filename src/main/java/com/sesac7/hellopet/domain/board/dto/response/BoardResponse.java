package com.sesac7.hellopet.domain.board.dto.response;

import com.sesac7.hellopet.domain.board.entity.BoardCategory;
import com.sesac7.hellopet.domain.board.entity.PetType;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardResponse {
    private Long id;
    private String nickname;
    private String title;
    private String content;
    private String image_url;

    private int likesCount;
    private int viewsCount;
    private int commentsCount;

    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    @NotNull(message = "카테고리 선택은 필수입니다.")
    private BoardCategory category;
    private PetType petType;

}
