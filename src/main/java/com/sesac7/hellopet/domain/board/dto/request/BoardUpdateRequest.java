package com.sesac7.hellopet.domain.board.dto.request;

import com.sesac7.hellopet.domain.board.entity.BoardCategory;
import com.sesac7.hellopet.domain.board.entity.PetType;
import lombok.Getter;

@Getter
public class BoardUpdateRequest {
    private String title;
    private String content;
    private String img_url;
    private BoardCategory boardCategory;
    private PetType petType;
}
