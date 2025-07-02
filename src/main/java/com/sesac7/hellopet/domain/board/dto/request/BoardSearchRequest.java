package com.sesac7.hellopet.domain.board.dto.request;

import com.sesac7.hellopet.domain.board.entity.BoardCategory;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardSearchRequest {
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private BoardCategory category = BoardCategory.TOTAL;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private SearchType searchType = SearchType.TOTAL;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private SortType sortType = SortType.CURRENT;

    private String keyword = "";
    private Integer page = 0; // 현재 페이지
    private Integer size = 10; // 게시글 목록 수

}
