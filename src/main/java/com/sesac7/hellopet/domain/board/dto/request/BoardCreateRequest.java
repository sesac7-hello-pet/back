package com.sesac7.hellopet.domain.board.dto.request;

import com.sesac7.hellopet.domain.board.entity.Board;
import com.sesac7.hellopet.domain.board.entity.BoardCategory;
import com.sesac7.hellopet.domain.board.entity.PetType;
import com.sesac7.hellopet.domain.user.entity.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Getter
public class BoardCreateRequest {

    @NotBlank
    private String title;
    @NotBlank
    private String content;
    private String img_url;

    @NotNull
    private BoardCategory boardCategory;
    @NotNull
    private PetType petType;

    public static Board from(@Valid BoardCreateRequest request, User user) {
        return new Board(
                null,
                request.getTitle(),
                request.getContent(),
                request.getImg_url(),
                0, 0, 0, LocalDateTime.now(), null,
                request.getBoardCategory(),
                request.getPetType(),
                user);
    }
}
