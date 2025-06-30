package com.sesac7.hellopet.domain.Board.dto.board.request;

import com.sesac7.hellopet.domain.Board.entity.BoardCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardSearchRequest {
    private BoardCategory category = BoardCategory.TOTAL;
    private SearchType searchType = SearchType.TOTAL;
    private String keyword;
    private String sort; // 정렬
    private Integer page = 0; // 현재 페이지
    private Integer size = 10; // 게시글 목록 수

}
