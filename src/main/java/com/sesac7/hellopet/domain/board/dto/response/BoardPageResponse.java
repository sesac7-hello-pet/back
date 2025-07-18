package com.sesac7.hellopet.domain.board.dto.response;

import com.sesac7.hellopet.domain.board.dto.request.BoardSearchRequest;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardPageResponse {
    private int page;
    private int size;
    private int totalPages;
    private Long totalCount;
    private List<BoardResponse> boardList = new ArrayList<>();

    public static BoardPageResponse from(Page<BoardResponse> page, BoardSearchRequest request) {
        return BoardPageResponse.builder()
                .page(request.getPage())
                .size(request.getSize())
                .totalPages(page.getTotalPages())
                .totalCount(page.getTotalElements())
                .boardList(page.getContent())
                .build();
    }
}
